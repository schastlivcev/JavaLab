package ru.kpfu.itis.rodsher.jlmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.stomp.StompHeaders;

public class Consumer {
    private String queue;
    private ObjectMapper objectMapper;
    private StompConnector connector;
    private MessageHandler messageHandler;

    private Consumer(StompConnector connector, ObjectMapper objectMapper) {
        this.connector = connector;
        this.objectMapper = objectMapper;
    }

    private void subscribeTo(String queue) {
        this.queue = queue;
        connector.subscribeConsumer(queue, this);
    }

    public void onReceive(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    protected void receive(Object o, StompHeaders messageHeaders) {
        StompHeaders headers = new StompHeaders();
        headers.setAck("client");
        headers.setMessageId(messageHeaders.getMessageId());
        headers.set("type", AcknowledgeType.ACCEPT.toString());
        connector.getSession().acknowledge(headers, true);
        System.out.println("Message[headers=" + messageHeaders + ", payload=" + o + "] started being consumed.");
        messageHandler.handleMessage(o, messageHeaders);
        headers.set("type", AcknowledgeType.COMPLETE.toString());
        connector.getSession().acknowledge(headers, true);
        System.out.println("Message[headers=" + messageHeaders + ", payload=" + o + "] ended being consumed.");
    }

    protected static SubscribeBuilder builder(StompConnector connector, ObjectMapper objectMapper) {
        return new Consumer(connector, objectMapper).new Builder();
    }

    public class Builder implements SubscribeBuilder, OnReceiveBuilder, FinalBuilder {

        private Builder() {}

        public OnReceiveBuilder subscribeTo(String queue) {
            Consumer.this.subscribeTo(queue);
            return this;
        }

        public FinalBuilder onReceive(MessageHandler messageHandler) {
            Consumer.this.onReceive(messageHandler);
            return this;
        }

        public Consumer build() {
            return Consumer.this;
        }
    }

    public interface SubscribeBuilder {
        OnReceiveBuilder subscribeTo(String queue);
    }

    public interface OnReceiveBuilder {
        FinalBuilder onReceive(MessageHandler messageHandler);
    }

    public interface FinalBuilder {
        Consumer build();
    }
}