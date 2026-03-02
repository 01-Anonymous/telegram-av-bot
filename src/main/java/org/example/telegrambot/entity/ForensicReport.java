package org.example.telegrambot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ForensicReport.java
 *
 * Huquq-tartibot organlariga topshirish uchun to'liq forensic ma'lumot.
 * BaseIncident'dan meros oladi, shuning uchun takrorlanish yo'q.
 */
@Entity
@Table(name = "forensic_report")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForensicReport extends BaseIncident {

    @Column(columnDefinition = "TEXT")
    private String rawMessageJson;     // To'liq Telegram update (AES-256 bilan shifrlanadi)

    private LocalDateTime exportedAt;  // Organlarga qachon topshirilgan
}