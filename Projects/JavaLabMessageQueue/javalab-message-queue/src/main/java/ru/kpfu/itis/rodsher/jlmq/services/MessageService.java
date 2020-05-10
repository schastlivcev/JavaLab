package ru.kpfu.itis.rodsher.jlmq.services;

import ru.kpfu.itis.rodsher.jlmq.dto.Dto;
import ru.kpfu.itis.rodsher.jlmq.dto.MessageDto;
import ru.kpfu.itis.rodsher.jlmq.models.MessageStatus;

public interface MessageService {
    Dto saveMessage(String queueName, String payload);
    Dto updateMessageStatus(Long messageId, MessageStatus status);
    Dto findAvailableMessageFor(String queue);
    Dto findMessageById(Long messageId);
}
