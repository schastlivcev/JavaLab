package ru.kpfu.itis.rodsher.jlmq;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

public class StompSessionFrameHandler implements StompSessionHandler {
    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        System.out.println("Con[id=" + stompSession.getSessionId() + "] opened.");
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
        System.out.println("Message[headers=" + stompHeaders + ", payload=" + o + "] received NOT from subscription!");
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders,
                                byte[] bytes, Throwable throwable) {
        System.out.println("Exception[type=" + throwable.getClass() + ", message=" + throwable.getMessage()
                + " occurred at Con[id=" + stompSession.getSessionId() + "] on Message[command="
                + stompCommand.toString() + ", headers=" + stompHeaders + ", payload=" + new String(bytes) + "].");
        throwable.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        System.out.println("Con[id=" + stompSession.getSessionId() + "] closed due to CloseStatus["
                + throwable.getMessage() + "]");
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return Object.class;
    }
}
