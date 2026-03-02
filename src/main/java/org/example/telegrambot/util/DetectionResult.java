// 1. Paket nomi — loyihangizning asosiy joylashuvi
package org.example.telegrambot.util;

// 2. Bu yerda hech qanday import kerak emas — chunki bu oddiy class, boshqa kutubxonaga bog'liq emas

/**
 * DetectionResult.java
 * <p>
 * Maqsad: Har qanday tekshiruv natijasini bitta obyekt sifatida qaytarish.
 * Nima uchun kerak?
 * - Kodni tartibli qilish uchun (alohida String, int va String qaytarmaymiz)
 * - Har bir service (URL, fayl, spam, klon) shu orqali natija qaytaradi
 * - Keyinchalik logging, moderatsiya va bot javobida ishlatamiz
 * - Oson o'qiladi va xato kamayadi
 *
 * @param verdict    3. Maydonlar — final bo'lgani uchun o'zgarmaydi (immutable — xavfsizroq) Natija turi: "SAFE", "SUSPICIOUS", "MALICIOUS"
 * @param confidence Ishonch darajasi: 0 dan 100 gacha
 * @param reason     Nima uchun shunday qaror chiqdi (log va foydalanuvchi uchun)
 */
public record DetectionResult(String verdict, int confidence, String reason) {

    public DetectionResult(String verdict, int confidence, String reason) {
        this.verdict = verdict;
        this.confidence = confidence;
        this.reason = reason != null ? reason : "";
    }


    public boolean isMalicious() {
        return "MALICIOUS".equalsIgnoreCase(verdict); // verdict "MALICIOUS" ga tengmi? (katta-kichik harf farqi yo'q)
    }

    public boolean isSuspicious() {
        return "SUSPICIOUS".equalsIgnoreCase(verdict);
    }

    public boolean isSafe() {
        return "SAFE".equalsIgnoreCase(verdict);
    }

    public boolean isError() {
        return "ERROR".equalsIgnoreCase(verdict);
    }

}