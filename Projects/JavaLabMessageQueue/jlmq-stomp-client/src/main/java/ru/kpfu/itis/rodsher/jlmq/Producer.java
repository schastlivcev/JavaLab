package ru.kpfu.itis.rodsher.jlmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.util.MimeTypeUtils;

public class Producer {
    private String queue;
    private ObjectMapper objectMapper;
    private StompConnector connector;

    private Producer(StompConnector connector, ObjectMapper objectMapper) {
        this.connector = connector;
        this.objectMapper = objectMapper;
    }

    private void toQueue(String queue) {
        this.queue = queue;
    }

    public void createTask(Object payload) {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.setContentType(MimeTypeUtils.APPLICATION_JSON);
        stompHeaders.setDestination("/app/" + queue);
        try {
            connector.getSession().send(stompHeaders, objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unacceptable payload format.", e);
        }
    }

    protected static QueueBuilder builder(StompConnector connector, ObjectMapper objectMapper) {
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