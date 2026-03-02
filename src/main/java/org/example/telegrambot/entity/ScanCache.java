package org.example.telegrambot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ScanCache.java
 *
 * Maqsad: Tekshirilgan URL yoki fayl natijalarini cache'lash.
 *
 * Bu entity tezlik uchun juda muhim.
 * CLEAN natijalar Redis'da saqlanadi (tezlik uchun).
 * MALICIOUS natijalar PostgreSQL + Redis'da saqlanadi (huquqiy uchun).
 */
@Entity
@Table(name = "scan_cache")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 64)
    private String hashKey;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Verdict verdict;


    @Column(length = 64)
    private String detectionSource;


    private Integer riskScore;

    @Column(nullable = false)
    private LocalDateTime checkedAt;

    private LocalDateTime expiresAt;

    @Column(length = 512)
    private String originalValue;
}