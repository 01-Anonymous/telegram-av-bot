package org.example.telegrambot.service.scan;

import org.example.telegrambot.service.CacheService;
import org.example.telegrambot.util.DetectionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * UrlhausScanService.java
 *
 * Maqsad: URLhaus API orqali havolani tekshirish (CacheService bilan birgalikda).
 *
 * Ishlash tartibi:
 * 1. Cache'dan qidirish
 * 2. Agar topilmasa — URLhaus API ga so‘rov jo‘natish
 * 3. Natijani cache'ga yozish
 * 4. Natijani qaytarish
 */
@Service
public class UrlhausScanService implements UrlScanService {

    private final CacheService cacheService;
    private final RestTemplate restTemplate;

    @Value("${api.urlhaus.url}")
    private String urlhausApiUrl;

    public UrlhausScanService(CacheService cacheService, RestTemplate restTemplate) {
        this.cacheService = cacheService;
        this.restTemplate = restTemplate;
    }

    @Override
    public DetectionResult scanUrl(String url) {
        // 1. Cache'dan qidirish (tezlik uchun)
        String cacheKey = generateCacheKey(url);
        DetectionResult cached = cacheService.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        // 2. URL ni tozalash (normalize qilish)
        String normalizedUrl = normalizeUrl(url);

        // 3. URLhaus API ga so‘rov jo‘natish
        DetectionResult result = checkUrlhaus(normalizedUrl);

        // 4. Natijani cache'ga yozish (24 soat muddat bilan)
        cacheService.put(cacheKey, result, 24, TimeUnit.HOURS);

        return result;
    }

    /**
     * URL ni tozalash (normalize qilish)
     * - http → https
     * - trailing slash olib tashlash
     * - query parametrlarni olib tashlash (hozircha oddiy)
     */
    private String normalizeUrl(String url) {
        if (url.startsWith("http://")) {
            url = url.replace("http://", "https://");
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * URLhaus API ga so‘rov jo‘natish
     * @param url — tekshiriladigan havola
     * @return DetectionResult — natija
     */
    private DetectionResult checkUrlhaus(String url) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            String body = "url=" + url;

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    urlhausApiUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String responseBody = response.getBody();
            if (responseBody != null && (responseBody.contains("malware_url") || responseBody.contains("is_host_url"))) {
                return new DetectionResult("MALICIOUS", 90, "URLhaus da zararli deb topildi");
            }

            return new DetectionResult("SAFE", 0, "URLhaus da topilmadi");
        } catch (Exception e) {
            return new DetectionResult("SUSPICIOUS", 50, "URLhaus API xatosi: " + e.getMessage());
        }
    }

    /**
     * Cache key generatsiyasi
     * Hozircha oddiy hashCode, keyinchalik SHA-256 qilamiz
     */
    private String generateCacheKey(String url) {
        return String.valueOf(url.hashCode());
    }


}