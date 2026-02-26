package org.example.telegramavbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * MaliciousDomain.java
 *
 * Maqsad: Zararli domenlarni (C2 serverlar, phishing saytlar, dropper yuklovchi domenlar) saqlash.
 *
 * Real misollar (siz bergan ekspertizadan):
 * - xarid-uz.com → BloodyWolf dropper payload delivery
 *
 * Bu entity botning domen asosidagi himoyasini kuchaytiradi.
 */
@Entity
@Table(name = "malicious_domain")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaliciousDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Zararli domen nomi (unikal)
     * Masalan: xarid-uz.com, t3legram-bonus.ru
     */
    @Column(nullable = false, unique = true, length = 255)
    private String domain;

    /**
     * Zararli oila nomi
     * Masalan: BloodyWolf, Banker, Dropper
     */
    @Column(length = 64)
    private String family;

    /**
     * Domen kategoriyasi
     * Masalan: C2, PHISHING, DROPPER, SPAM
     */
    @Column(length = 32)
    private String category;

    /**
     * Ishonch darajasi (0-100)
     * 100 = 100% tasdiqlangan zararli
     */
    @Column(nullable = false)
    private Integer confidenceScore;

    /**
     * Qayerdan olingan?
     * Masalan: "Manual", "URLhaus", "SudEkspertizasi"
     */
    @Column(length = 64)
    private String source;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    /**
     * Muddati tugash vaqti
     * NULL = cheksiz saqlanadi
     */
    private LocalDateTime expiresAt;

    /**
     * Noto'g'ri bloklanganmi?
     * 3 ta false positive → avtomatik whitelist'ga o'tadi
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