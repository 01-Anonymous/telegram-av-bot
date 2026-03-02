package org.example.telegrambot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * UserCache.java
 *
 * Maqsad: Foydalanuvchi haqida asosiy ma'lumotlarning "snapshot"ini saqlash.
 *
 * Nima uchun kerak?
 * - Cloning (nusxa) tekshirish uchun oldingi username va ismni saqlash
 * - Foydalanuvchining faolligini kuzatish
 * - Statistika va monitoring uchun
 */
@Entity
@Table(name = "user_cache")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCache {

    @Id
    private Long telegramUserId;           // Telegram ID — Primary Key (o'zgarmas)

    @Column(length = 64)
    private String username;               // Joriy username (o'zgarishi mumkin)

    @Column(length = 128)
    private String firstName;

    @Column(length = 128)
    private String lastName;

    @Column(nullable = false)
    private LocalDateTime firstSeen;       // Birinchi marta botga kelgan vaqti

    @Column(nullable = false)
    private LocalDateTime lastSeen;        // Oxirgi marta faol bo'lgan vaqti

    @Builder.Default
    private Integer violationCount = 0;    // Necha marta qoidani buzgan

    @Column(nullable = false)
    @Builder.Default
    private Boolean isBanned = false;      // Hozir ban holatidami?

    private LocalDateTime banUntil;        // Qachongacha ban (NULL = permanent emas)

    // Qo'shimcha izoh (ixtiyoriy)
    @Column(length = 512)
    private String notes;
}