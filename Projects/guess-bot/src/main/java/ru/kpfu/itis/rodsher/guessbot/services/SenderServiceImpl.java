package ru.kpfu.itis.rodsher.guessbot.services;

import net.dv8tion.jda.api.JDA;
import org.aspectj.apache.bcel.classfile.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.kpfu.itis.rodsher.guessbot.models.Channel;
import ru.kpfu.itis.rodsher.guessbot.models.ChannelType;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;
import ru.kpfu.itis.rodsher.guessbot.repositories.ChannelsRepository;

import java.util.List;

@Service
public class SenderServiceImpl implements SenderService {

    @Autowired
    private JDA discordBot;

    @Autowired
    private TelegramLongPollingBot telegramBot;

    @Autowired
    private ChannelsRepository channelsRepository;

    @Override
    public void sendDirectMessage(ClientType clientType, String channelId, ChannelType channelType, String message) {
        switch (clientType) {
            case DISCORD:
                if(channelType.equals(ChannelType.PRIVATE)) {
                    discordBot.getPrivateChannelById(channelId).sendMessage(message).queue();
                    break;
                }
                discordBot.getTextChannelById(channelId).sendMessage(message).queue();
                break;
            case TELEGRAM:
                try {
                    telegramBot.execute(new SendMessage(channelId, message));
                } catch (TelegramApiException e) {
                    throw new IllegalStateException("Failed to send message in Telegram.", e);
                }
                break;
        }
    }

    @Override
    public synchronized void sendMessageToAll(String message) {
        List<Channel> channels = channelsRepository.findAll();
        for(Channel channel : channels) {
            switch (channel.getClientType()) {
                case DISCORD:
                    if(channel.getType().equals(ChannelType.PRIVATE)) {
                        discordBot.getPrivateChannelById(channel.getClientId()).sendMessage(message).queue();
                        break;
                    }
                    discordBot.getTextChannelById(channel.getClientId()).sendMessage(message).queue();
                    break;
                case TELEGRAM:
                    try {
                        telegramBot.execute(new SendMessage(channel.getClientId(), message));
                    } catch (TelegramApiException e) {
                        throw new IllegalStateException("Failed to send message in Telegram.", e);
                    }
                    break;
            }
        }
    }

    @Override
    public void sendMessageToAllExcluding(ClientType clientType, String channelId, String message) {
        List<Channel> channels = channelsRepository.findAll();
        for(Channel channel : channels) {
            if(!channel.getClientType().equals(clientType) && !channel.getClientId().equals(channelId)) {
                switch (channel.getClientType()) {
                    case DISCORD:
                        if(channel.getType().equals(ChannelType.PRIVATE)) {
                            discordBot.getPrivateChannelById(channel.getClientId()).sendMessage(message).queue();
                            break;
                        }
                        discordBot.getTextChannelById(channel.getClientId()).sendMessage(message).queue();
                        break;
                    case TELEGRAM:
                        try {
                            telegramBot.execute(new SendMessage(channel.getClientId(), message));
                        } catch (TelegramApiException e) {
                            throw new IllegalStateException("Failed to send message in Telegram.", e);
                        }
                        break;
                }
            }
        }
    }
}