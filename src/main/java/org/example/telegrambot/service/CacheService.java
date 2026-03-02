package org.example.telegrambot.service;

import org.example.telegrambot.util.DetectionResult;

import java.util.concurrent.TimeUnit;

public interface CacheService {

    DetectionResult get (String key);

    void put (String key, DetectionResult result, long ttlSeconds, TimeUnit timeUnit);

    void invalidate (String key);

    long size();
}
