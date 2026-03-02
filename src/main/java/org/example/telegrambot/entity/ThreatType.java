package org.example.telegrambot.entity;

/**
 * ThreatType.java
 *
 * Maqsad: Tahdid turini aniq va xavfsiz belgilash.
 * String o'rniga enum ishlatish — xato yozish ehtimolini kamaytiradi.
 */
public enum ThreatType {

    MALWARE,           // Zararli dastur (virus, troyan, dropper)
    PHISHING,          // Fishing (aldovchi sayt)
    SPAM,              // Oddiy spam (18+, reklama)
    BANKER_APK,        // Android banker troyani
    DROPPER,           // Dropper (boshqa zararli yuklaydigan)
    CLONING,           // Klon akkaunt
    REACTION_SPAM,     // Reaktsiya spam
    DOUBLE_EXTENSION,  // .PDF.apk kabi maskirovka
    SUSPICIOUS_LINK,   // Shubhali link
    UNKNOWN            // Noma'lum tahdid
}