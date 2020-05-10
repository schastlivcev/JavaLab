package ru.kpfu.itis.rodsher.jlmq;

import org.springframework.messaging.simp.stomp.StompHeaders;

@FunctionalInterface
public interface MessageHandler {
    void handleMessage(Object payload, StompHeaders messageHeaders);
}
