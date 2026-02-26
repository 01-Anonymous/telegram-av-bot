package org.example.telegramavbot.config;
import lombok.extern.slf4j.Slf4j;
import org.example.telegramavbot.BotHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(BotHandler botHandler) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        botsApi.registerBot(botHandler);

        log.info(" Bot muvaffaqiyatli ro'yxatdan o'tdi: {}", botHandler.getBotUsername());
        return botsApi;

    }

}