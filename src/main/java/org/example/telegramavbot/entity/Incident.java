package org.example.telegramavbot.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Incident.java
 *
 * Oddiy operatsion log.
 * Tezlik va monitoring uchun ishlatiladi.
 * ForensicReport dan yengilroq.
 */
@Entity
@Table(name = "incident")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Incident extends BaseIncident {

    @Column(length = 512)
    private String reason;   // Nima uchun bloklangan (qo'shimcha izoh)
}