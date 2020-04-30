package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.Message;

import java.util.List;
import java.util.Optional;

public interface MessagesRepository {
    Long save(Message message);
    List<Message> findMessagesByChannelId(Long channelId);
    Optional<Message> find(Long id);
    List<Message> findLastMessagesForChannelsByUserId(Long userId);
}
