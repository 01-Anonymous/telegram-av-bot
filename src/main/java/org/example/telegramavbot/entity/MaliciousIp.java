package org.example.telegramavbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * MaliciousIp.java
 *
 * Maqsad: Zararli C2 (Command & Control) serverlarining IP manzillarini saqlash.
 *
 * Real misollar (siz bergan ekspertizadan):
 * - 185.196.8.49 → BloodyWolf C2 server
 * - 89.22.224.180 → Banker troyan C2
 * - 77.221.138.47 → Banker troyan C2
 */
@Entity
@Table(name = "malicious_ip")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaliciousIp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String ipAddress;               // IPv4 yoki IPv6 manzil

    @Column(length = 64)
    private String family;                  // BloodyWolf, Banker, Dropper va h.k.

    @Column(length = 32)
    private String category;                // C2, PHISHING, DROPPER, SPAM

    @Column(nullable = false)
    private Integer confidenceScore;        // 0-100 (qanchalik ishonchli)

    @Column(length = 64)
    private String source;                  // "Manual", "SudEkspertizasi", "MalwareBazaar" va h.k.

    @Column(length = 128)
    private String asn;                     // ASN (Autonomous System Number) — masalan: AS12345

    @Column(length = 64)
    private String country;                 // Shvetsariya, Rossiya va h.k.

    @Column(nullable = false)
    private LocalDateTime addedAt;

    private LocalDateTime expiresAt;        // NULL = cheksiz

    @Column(nullable = false)
    private Boolean falsePositiveFlag = false;

    @Column(length = 64)
    private String addedBy;                 // Kim qo'shgan (admin username)

    @Column(length = 512)
    private String notes;                   // Qo'shimcha izoh (masalan: "BloodyWolf C2")
}