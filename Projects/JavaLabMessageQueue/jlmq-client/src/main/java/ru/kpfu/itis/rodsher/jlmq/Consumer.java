package ru.kpfu.itis.rodsher.jlmq;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Consumer {
    private String queue;
    private ObjectMapper objectMapper;
    private Connector connector;
    private MessageHandler messageHandler;

    private Consumer(Connector connector, ObjectMapper objectMapper) {
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

    protected void receive(MessageDto message) {
        connector.sendMessage(MessageDto.builder().setCommand(Command.ACCEPT)
                .setMessageId(message.getMessageId()).build());
        System.out.println("Message[" + message.toJson() + "] started being consumed.");
        messageHandler.handleMessage(message);
        connector.sendMessage(MessageDto.builder().setCommand(Command.COMPLETE)
                .setMessageId(message.getMessageId()).build());
        System.out.println("Message[" + message.toJson() + "] ended being consumed.");
    }

    protected static SubscribeBuilder builder(Connector connector, ObjectMapper objectMapper) {
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