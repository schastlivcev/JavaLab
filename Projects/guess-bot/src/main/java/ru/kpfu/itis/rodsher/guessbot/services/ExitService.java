package ru.kpfu.itis.rodsher.guessbot.services;

import ru.kpfu.itis.rodsher.guessbot.dto.Dto;
import ru.kpfu.itis.rodsher.guessbot.models.ChannelType;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;

public interface ExitService {
    void exitUser(ClientType clientType, String channelId, ChannelType channelType, String clientId);
}
