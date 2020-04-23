package ru.kpfu.itis.rodsher.services;

import ru.kpfu.itis.rodsher.dto.Dto;

public interface ChatService {
    Dto createChannel(String name);
    Dto getChannel(Long id);
    Dto getAllChannels();
    Dto addUsersToChannel(Long channelId, Long... userId);
    Dto saveMessage(Long channelId, Long userAuthorId, String content);
    Dto loadMessagesForChannel(Long channelId);
    Dto getMessage(Long messageId);
    Dto checkIfChannelExistsForUsers(Long userId1, Long userId2);
    Dto getUsersForChannel(Long channelId);
}
