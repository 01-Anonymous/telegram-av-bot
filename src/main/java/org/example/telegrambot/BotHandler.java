package org.example.telegrambot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BotHandler extends TelegramLongPollingBot {

    private final String botUsername;

    // Foydalanuvchi qaysi holatda ekanligini saqlaymiz
    // KEY: chatId, VALUE: holat ("wait_url", "wait_file", "wait_clone", "wait_report")
    private final Map<String, String> userState = new ConcurrentHashMap<>();

    public BotHandler(
            @Value("${bot.token}") String botToken,
            @Value("${bot.username}") String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {

        // Oddiy text xabar
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update);
        }

        // Fayl yuborilsa
        if (update.hasMessage() && update.getMessage().hasDocument()) {
            handleFileMessage(update);
        }
    }

    // ─── TEXT XABAR ──────────────────────────────────────────────
    private void handleTextMessage(Update update) {
        String text = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();

        // Guruh bo'lsa — hozircha oddiy javob
        if (!update.getMessage().getChat().isUserChat()) {
            sendText(chatId, "✅ CyberQalqon guruhda ham kuzatyapti.");
            return;
        }

        // Foydalanuvchi biror narsani kutayotgan bo'lsa (state machine)
        String state = userState.get(chatId);
        if (state != null) {
            handleUserInput(chatId, text, state);
            return;
        }

        // Tugma matniga qarab harakat
        switch (text) {

            case "/start":
                sendMainMenu(chatId);
                break;

            case "🔍 Havola tekshirish":
                userState.put(chatId, "wait_url");
                sendText(chatId, "🔗 Tekshirish uchun havolani yuboring:");
                break;

            case "📁 Fayl tekshirish":
                userState.put(chatId, "wait_file");
                sendText(chatId, "📁 Tekshirish uchun faylni yuboring (10 MB gacha):");
                break;

            case "👤 Klon tekshirish":
                userState.put(chatId, "wait_clone");
                sendText(chatId, "👤 Shubhali username ni yuboring:\n\nMasalan: @username yoki username");
                break;

            case "📊 Statistika":
                sendStatistics(chatId);
                break;

            case "📋 Qo'llanma":
                sendText(chatId,
                        "📋 Qo'llanma:\n\n" +
                                "🔍 Havola tekshirish — shubhali linkni yuboring\n" +
                                "📁 Fayl tekshirish — .apk, .exe, .zip va h.k. (10MB gacha)\n" +
                                "👤 Klon tekshirish — shubhali username ni yuboring\n" +
                                "📊 Statistika — bugungi hisobot\n\n" +
                                "⚠️ Ehtiyot bo'ling!\n" +
                                "Hech qanday shubhali havolani ochmang!");
                break;

            case "🚨 Shikoyat yuborish":
                userState.put(chatId, "wait_report");
                sendText(chatId, "🚨 Muammoni tavsiflang va yuboring:");
                break;

            default:
                // Link bo'lsa — avtomatik tekshir
                if (text.startsWith("http://") || text.startsWith("https://") || text.startsWith("t.me")) {
                    sendText(chatId, "🔍 Link tekshirilmoqda...\n⏳ Natija tez orada.");
                    // TODO: UrlScanService.scan(text)
                } else {
                    sendMainMenu(chatId);
                }
                break;
        }
    }

    // ─── STATE MACHINE — FOYDALANUVCHI INPUT ─────────────────────
    private void handleUserInput(String chatId, String text, String state) {
        switch (state) {

            case "wait_url":
                userState.remove(chatId);
                if (text.startsWith("http://") || text.startsWith("https://") || text.startsWith("t.me")) {
                    sendText(chatId, "🔍 Tekshirilmoqda: " + text + "\n⏳ Natija tez orada...");
                    // TODO: UrlScanService.scan(text)
                } else {
                    sendText(chatId, "⚠️ Bu havola emas. To'g'ri link yuboring.");
                }
                break;

            case "wait_file":
                userState.remove(chatId);
                sendText(chatId, "⚠️ Iltimos fayl yuboring, matn emas.");
                break;

            case "wait_clone":
                userState.remove(chatId);
                String username = text.replace("@", "").trim();
                sendText(chatId, "👤 @" + username + " tekshirilmoqda...\n⏳ Natija tez orada.");
                // TODO: CloningGuardService.check(username)
                break;

            case "wait_report":
                userState.remove(chatId);
                sendText(chatId, "✅ Shikoyatingiz qabul qilindi. Rahmat!\nTez orada ko'rib chiqamiz.");
                // TODO: Admin log kanaliga yuborish
                break;

            default:
                userState.remove(chatId);
                sendMainMenu(chatId);
                break;
        }
    }

    // ─── FAYL XABAR ──────────────────────────────────────────────
    private void handleFileMessage(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String fileName = update.getMessage().getDocument().getFileName();
        long fileSize = update.getMessage().getDocument().getFileSize();

        // 10MB limit
        if (fileSize > 10 * 1024 * 1024) {
            sendText(chatId, "❌ Fayl hajmi 10MB dan katta. Kichikroq fayl yuboring.");
            userState.remove(chatId);
            return;
        }

        userState.remove(chatId);
        sendText(chatId,
                "📁 Fayl qabul qilindi!\n" +
                        "📄 Nomi: " + fileName + "\n" +
                        "📦 Hajmi: " + (fileSize / 1024) + " KB\n" +
                        "⏳ Tekshirilmoqda...");
        // TODO: FileScanService.scan(fileId, fileName)
    }

    // ─── STATISTIKA ───────────────────────────────────────────────
    private void sendStatistics(String chatId) {
        // TODO: DB dan real ma'lumot olish
        sendText(chatId,
                "📊 Statistika:\n\n" +
                        "🔍 Tekshirilgan havolalar: —\n" +
                        "📁 Tekshirilgan fayllar: —\n" +
                        "🚫 Bloklangan tahdidlar: —\n" +
                        "👤 Aniqlangan klonlar: —\n\n" +
                        "⏳ Tez orada real statistika qo'shiladi.");
    }

    // ─── ASOSIY MENYU ─────────────────────────────────────────────
    private void sendMainMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("🛡 Salom! CyberQalqon botga xush kelibsiz.\n\nQuyidagilardan birini tanlang:");

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);  // Ekranga mos o'lcham
        markup.setIsPersistent(true);       // Har doim pastda ko'rinib turadi
        markup.setSelective(false);

        List<KeyboardRow> rows = new ArrayList<>();

        // 1-qator: Havola + Fayl
        KeyboardRow row1 = new KeyboardRow();
        row1.add("🔍 Havola tekshirish");
        row1.add("📁 Fayl tekshirish");
        rows.add(row1);

        // 2-qator: Klon + Statistika
        KeyboardRow row2 = new KeyboardRow();
        row2.add("👤 Klon tekshirish");
        row2.add("📊 Statistika");
        rows.add(row2);

        // 3-qator: Qo'llanma + Shikoyat
        KeyboardRow row3 = new KeyboardRow();
        row3.add("📋 Qo'llanma");
        row3.add("🚨 Shikoyat yuborish");
        rows.add(row3);

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // ─── YORDAMCHI ────────────────────────────────────────────────
    private void sendText(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}