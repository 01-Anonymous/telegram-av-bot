package org.example.telegramavbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * MaliciousHash.java
 *
 * Maqsad: Zararli fayllarning hash'larini (IOC) saqlash.
 *
 * Real misollar (siz bergan ekspertizadan):
 * - DavlatXarid.jar → SHA-1: 8568fe78725efaafacec9fa81bcd3068ae4d6f10
 * - Sud Qarori.PDF.apk → SHA-1: f342ace35077a80e648b6b172c13ce7f490902e4
 *
 * Bu entity botning eng kuchli qurollaridan biri bo'ladi.
 */
@Entity
@Table(name = "malicious_hash")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaliciousHash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Faylning hash'i (eng muhim maydon)
     * SHA-256 tavsiya qilinadi, lekin SHA-1 ham saqlash mumkin
     */
    @Column(nullable = false, unique = true, length = 64)
    private String hashValue;

    /**
     * Hash turi: SHA256 yoki SHA1
     */
    @Column(length = 10)
    private String hashType;

    /**
     * Zararli oila nomi (family)
     * Masalan: BloodyWolf, Banker, Dropper
     */
    @Column(length = 64)
    private String family;

    /**
     * Tahdid turi
     */
    @Column(length = 32)
    private String threatType;

    /**
     * Qayerdan olingan?
     * Masalan: "Manual", "SudEkspertizasi", "MalwareBazaar"
     */
    @Column(length = 64)
    private String source;

    /**
     * Ishonch darajasi (0-100)
     * 100 = 100% tasdiqlangan zararli
     */
    @Column(nullable = false)
    private Integer confidenceScore;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    /**
     * Muddati tugash vaqti
     * NULL = cheksiz saqlanadi
     */
    private LocalDateTime expiresAt;

    /**
     * Noto'g'ri bloklanganmi?
     * Admin 3 marta false positive belgilasa → avtomatik whitelist'ga o'tadi
     */
    @Column(nullable = false)
    private Boolean falsePositiveFlag = false;

    /**
     * Kim qo'shgan? (admin username)
     */
    @Column(length = 64)
    private String addedBy;

    /**
     * Qo'shimcha izoh
     */
    @Column(length = 512)
    private String notes;
}