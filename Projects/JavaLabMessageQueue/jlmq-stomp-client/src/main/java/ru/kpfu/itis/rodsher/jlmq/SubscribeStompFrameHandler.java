package ru.kpfu.itis.rodsher.jlmq;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;
import java.util.Optional;

public class SubscribeStompFrameHandler implements StompFrameHandler {
    private final StompConnector connector;

    public SubscribeStompFrameHandler(StompConnector connector) {
        this.connector = connector;
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return Object.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
        System.out.print("Message[payload=" + o + "] from subscription ");
        String queue = null;
        try {
            queue = stompHeaders.getDestination().substring("/topic/".length());
        } catch (NullPointerException | StringIndexOutOfBoundsException e) {
            System.out.println(" doesn't contain Queue name!");
        }
        if(queue != null) {
            Optional<Consumer> consumerOptional = connector.getConsumerManager()
                    .getConsumerFor(queue);
            if(consumerOptional.isPresent()) {
                Consumer consumer = consumerOptional.get();
                System.out.println("has been given to Consumer[queue=" + queue + "].");
                consumer.receive(o, stompHeaders);
                return;
            }
        }
        System.out.println("has no Consumer for such queue.");
    }
}