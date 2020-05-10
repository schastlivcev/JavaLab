package ru.kpfu.itis.rodsher.jlmq;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Producer {
    private String queue;
    private ObjectMapper objectMapper;
    private Connector connector;

    private Producer(Connector connector, ObjectMapper objectMapper) {
        this.connector = connector;
        this.objectMapper = objectMapper;
    }

    private void toQueue(String queue) {
        this.queue = queue;
    }

    public void createTask(Object payload) {
        connector.sendMessage(MessageDto.builder().setCommand(Command.SEND).setQueue(queue).setPayload(payload).build());
    }

    protected static QueueBuilder builder(Connector connector, ObjectMapper objectMapper) {
        return new Producer(connector, objectMapper).new Builder();
    }

    public class Builder implements QueueBuilder, FinalBuilder {

        private Builder() {}

        public Builder toQueue(String queue) {
            Producer.this.toQueue(queue);
            return this;
        }

        public Producer build() {
            return Producer.this;
        }
    }

    public interface QueueBuilder {
        FinalBuilder toQueue(String queue);
    }

    public interface FinalBuilder {
        Producer build();
    }
}