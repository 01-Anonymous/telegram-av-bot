package org.example.telegrambot.entity;

public enum Verdict {
    CLEAN,      // Fayl xavfsiz deb topildi
    MALICIOUS,  // Fayl zararli deb topildi
    SUSPICIOUS, // Fayl shubhali deb topildi
    ERROR       // Skanningda xatolik yuz berdi
}
