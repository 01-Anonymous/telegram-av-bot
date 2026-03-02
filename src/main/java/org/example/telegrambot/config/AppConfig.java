package org.example.telegrambot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfig.java
 *
 * Maqsad: Spring Boot'da kerakli bean'larni belgilash.
 * Hozircha RestTemplate bean'ini yaratamiz — bu HTTP so'rovlar uchun kerak.
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate bean'i — Spring tomonidan yaratiladi va boshqa class'larga inject qilinadi.
     * Bu bean API'ga (URLhaus, PhishTank va h.k.) so'rov jo'natish uchun ishlatiladi.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();  // Oddiy RestTemplate yaratamiz
    }
}