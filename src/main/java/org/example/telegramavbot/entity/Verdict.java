package org.example.telegramavbot.entity;

public enum Verdict {
    CLEAN,      // Fayl xavfsiz deb topildi
    MALICIOUS,  // Fayl zararli deb topildi
    SUSPICIOUS, // Fayl shubhali deb topildi
    ERROR       // Skanningda xatolik yuz berdi
}
