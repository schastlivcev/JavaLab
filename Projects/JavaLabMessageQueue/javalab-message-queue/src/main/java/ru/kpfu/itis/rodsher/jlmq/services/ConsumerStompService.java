package ru.kpfu.itis.rodsher.jlmq.services;

import ru.kpfu.itis.rodsher.jlmq.dto.Dto;

import java.util.Set;

public interface ConsumerStompService {
    Dto subscribeConsumer(String queue, String sessionId);
    Dto sendMessageForConsumerIfAvailableOn(String queue);
    Dto completeMessage(Long messageId);
    Dto acceptMessage(Long messageId);
    void removeConsumersBySession(String sessionId);
    Set<String> getQueues();
}
