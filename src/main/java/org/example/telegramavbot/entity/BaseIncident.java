package org.example.telegramavbot.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected Long telegramUserId;

    @Column(length = 64)
    protected String usernameSnapshot;

    @Column(length = 128)
    protected String fullNameSnapshot;

    @Column(nullable = false)
    protected Long chatId;

    @Column(length = 255)
    protected String chatTitleSnapshot;

    @Column(nullable = false)
    protected Long messageId;

    @Column(nullable = false)
    protected LocalDateTime detectedAt;

    @Column(nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    protected ThreatType threatType;        // Endi enum!

    @Column(length = 64)
    protected String detectionSource;

    protected Integer riskScore;

    @Column(length = 32)
    protected String actionTaken;
}