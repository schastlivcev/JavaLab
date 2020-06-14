package ru.kpfu.itis.rodsher.guessbot.services;


import ru.kpfu.itis.rodsher.guessbot.dto.Dto;
import ru.kpfu.itis.rodsher.guessbot.models.ChannelType;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;
import ru.kpfu.itis.rodsher.guessbot.models.User;

public interface JoinService {
    Dto joinUser(ClientType clientType, String channelId, ChannelType channelType, String userId, String name);
    void disconnectAll();
    void disconnectUser(User user);
}