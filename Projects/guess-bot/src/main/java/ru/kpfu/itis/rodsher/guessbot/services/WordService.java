package ru.kpfu.itis.rodsher.guessbot.services;

import ru.kpfu.itis.rodsher.guessbot.models.ChannelType;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;

public interface WordService {
    void handleWord(ClientType clientType, String channelId, ChannelType channelType, String clientId, String word);
    void handleDescription(ClientType clientType, String channelId, ChannelType channelType, String clientId, String description);
    void handleGuess(ClientType clientType, String channelId, ChannelType channelType, String clientId, String guess);
}
