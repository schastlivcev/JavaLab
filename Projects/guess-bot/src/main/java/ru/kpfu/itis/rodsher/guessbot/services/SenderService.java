package ru.kpfu.itis.rodsher.guessbot.services;

import ru.kpfu.itis.rodsher.guessbot.models.ChannelType;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;

public interface SenderService {
    void sendDirectMessage(ClientType clientType, String channelId, ChannelType channelType, String message);
    void sendMessageToAll(String message);
    void sendMessageToAllExcluding(ClientType clientType, String channelId, String message);
}
