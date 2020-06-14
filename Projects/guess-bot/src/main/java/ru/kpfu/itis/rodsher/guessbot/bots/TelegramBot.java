package ru.kpfu.itis.rodsher.guessbot.bots;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.kpfu.itis.rodsher.guessbot.models.ChannelType;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;
import ru.kpfu.itis.rodsher.guessbot.services.MessageHandler;

public class TelegramBot extends TelegramLongPollingBot {

    private final String token;

    private final MessageHandler messageHandler;

    public TelegramBot(String token, MessageHandler messageHandler) {
        super();
        this.token = token;
        this.messageHandler = messageHandler;
    }

    public TelegramBot(String token, MessageHandler messageHandler, DefaultBotOptions options) {
        super(options);
        this.token = token;
        this.messageHandler = messageHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            ChannelType channelType = ChannelType.GROUP;
            if(update.getMessage().getChat().isUserChat()) {
                channelType = ChannelType.PRIVATE;
            }
            messageHandler.handleMessage(ClientType.TELEGRAM, Long.toString(update.getMessage().getChat().getId()),
                    channelType, Integer.toString(update.getMessage().getFrom().getId()), update.getMessage().getText());
        }
    }

    @Override
    public String getBotUsername() {
        return "GuessWordBot";
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
