package ru.kpfu.itis.rodsher.jlmq.services;

import org.springframework.web.socket.WebSocketSession;
import ru.kpfu.itis.rodsher.jlmq.dto.Dto;

public interface ConsumerService {
    Dto subscribeConsumer(String queue, WebSocketSession session);
    Dto sendMessageForConsumerIfAvailableOn(String queue);
    Dto completeMessage(Long messageId);
    Dto acceptMessage(Long messageId);
    void removeConsumersBySession(WebSocketSession session);
}
