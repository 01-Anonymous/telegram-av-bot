package org.example.telegrambot.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.example.telegrambot.util.DetectionResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@Service
public class CaffeineCacheService implements CacheService {

    private final Cache<String, DetectionResult> cleanCache = Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .maximumSize(10_000)
            .build();

    private final Cache<String, DetectionResult> maliciousCache = Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .maximumSize(5_000)
            .build();



    @Override
    public DetectionResult get(String key) {
        DetectionResult malicious = maliciousCache.getIfPresent(key);
        if (malicious != null) {
            return malicious;
        }
        return cleanCache.getIfPresent(key);
    }

    @Override
    public void put(String key, DetectionResult result, long ttl, TimeUnit unit) {
        if (result.isMalicious()) {
            maliciousCache.put(key, result);
        } else if (result.isSafe()) {
            cleanCache.put(key, result);
        }
        // SUSPICIOUS yoki ERROR — hozircha cache'ga yozilmaydi (keyinchalik qo'shamiz)
    }

    @Override
    public void invalidate(String key) {
        maliciousCache.invalidate(key);
        cleanCache.invalidate(key);
    }

    @Override
    public long size() {
        return cleanCache.estimatedSize() + maliciousCache.estimatedSize();
    }
}
