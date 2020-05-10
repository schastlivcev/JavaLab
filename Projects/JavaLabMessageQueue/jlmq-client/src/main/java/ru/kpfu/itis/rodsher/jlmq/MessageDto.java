package ru.kpfu.itis.rodsher.jlmq;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE,
    fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MessageDto {
    private Map<String, Object> map;

    private static final ObjectMapper objectMapper;

    private static final String COMMAND = "command";
    private static final String QUEUE = "queue";
    private static final String MESSAGE_ID = "messageId";
    private static final String PAYLOAD = "payload";

    static {
        objectMapper = new ObjectMapper();
    }

    private MessageDto() {
        map = new LinkedHashMap<>();
    }

    private MessageDto(Map<String, Object> map) {
        this();
        this.map = map;
    }

    public Command getCommand() {
        return (Command) map.get(COMMAND);
    }

    public void setCommand(Command command) {
        map.put(COMMAND, command);
    }

    public String getQueue() {
        return (String) map.get(QUEUE);
    }

    public void setQueue(String queue) {
        map.put(QUEUE, queue);
    }

    public Long getMessageId() {
        return (Long) map.get(MESSAGE_ID);
    }

    public void setMessageId(Long messageId) {
        map.put(MESSAGE_ID, messageId);
    }

    public TreeNode getPayload() {
        return (TreeNode) map.get(PAYLOAD);
    }

    public void setPayload(Object payload) {
        if(payload != null) {
            map.put(PAYLOAD, objectMapper.valueToTree(payload));
        }
    }

    public Object get(String key) {
        return map.get(key);
    }

    public void set(String key, Object value) {
        map.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDto message = (MessageDto) o;
        return map.equals(message.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    public String toJson() {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unserializable message data.", e);
        }
    }

    public static MessageDto fromJson(String json) throws IllegalArgumentException {
        try {
            Map<String, Object> map = objectMapper.readValue(json, Map.class);
            if(map.get(PAYLOAD) != null) {
                map.put(PAYLOAD, objectMapper.valueToTree(map.get(PAYLOAD)));
            }
            if(map.get(COMMAND) != null) {
                map.put(COMMAND, Command.valueOf((String) map.get(COMMAND)));
            }
            if(map.get(MESSAGE_ID) != null) {
                map.put(MESSAGE_ID, Long.valueOf(map.get(MESSAGE_ID).toString()));
            }
            return new MessageDto(map);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            throw new IllegalArgumentException("Unserializable message data.", e);
        }
    }

    @Override
    public String toString() {
        return "Message" + map;
    }

    public static CommandBuilder builder() {
        return new MessageDto().new Builder();
    }

    public class Builder implements CommandBuilder, FinalBuilder {
        private MessageDto message;

        private Builder() {}

        public FinalBuilder setCommand(Command command) {
            MessageDto.this.setCommand(command);
            return this;
        }

        public FinalBuilder setQueue(String queue) {
            MessageDto.this.setQueue(queue);
            return this;
        }

        public FinalBuilder setMessageId(Long messageId) {
            MessageDto.this.setMessageId(messageId);
            return this;
        }

        public FinalBuilder setPayload(Object payload) {
            MessageDto.this.setPayload(payload);
            return this;
        }

        public FinalBuilder set(String key, Object value) {
            MessageDto.this.set(key, value);
            return this;
        }

        public MessageDto build() {
            return MessageDto.this;
        }
    }

    public interface CommandBuilder {
        FinalBuilder setCommand(Command command);
    }

    public interface FinalBuilder {
        FinalBuilder setQueue(String queue);
        FinalBuilder setMessageId(Long messageId);
        FinalBuilder setPayload(Object payload);
        FinalBuilder set(String key, Object value);
        MessageDto build();
    }
}