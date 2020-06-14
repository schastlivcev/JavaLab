package ru.kpfu.itis.rodsher.guessbot.services;

import ru.kpfu.itis.rodsher.guessbot.models.ChannelType;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;

public interface MessageHandler {
    void handleMessage(ClientType clientType, String channelId, ChannelType channelType, String userId, String message);
}
