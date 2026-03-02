package org.example.telegrambot.service.scan;

import org.example.telegrambot.util.DetectionResult;

public interface UrlScanService {

    DetectionResult scanUrl(String url);
}
