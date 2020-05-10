package ru.kpfu.itis.rodsher.jlmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

public class Connector {
    private final ObjectMapper objectMapper;
    private final ConsumerManager consumerManager;
    private final InfiniteThread infiniteThread;

    private WebSocketClient client;
    private WebSocketSession session;
    private URI uri;

    private Connector() {
        objectMapper = new ObjectMapper();
        client = new StandardWebSocketClient();
        consumerManager = new ConsumerManager();
        infiniteThread = new InfiniteThread();
    }

    protected void onUri(URI uri) {
        this.uri = uri;
    }

    protected void connect() {
        try {
            session = client.doHandshake(new WebSocketClientHandler(this), null, uri).get();
            infiniteThread.start();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Failed to connect to server on URI: " + uri);
        }
    }

    public void disconnect() {
        infiniteThread.interrupt();
        if(session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                return;
            }
        }
    }

    protected synchronized void sendMessage(String message) {
        if(session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                if(!session.isOpen()) {
                    System.out.println("Message sending was prevented by connection closing.");
                } else {
                    throw new IllegalStateException("Refused to send message due to connection error.", e);
                }
            }
        }
    }

    protected void subscribeConsumer(String queue, Consumer consumer) {
        consumerManager.addConsumer(queue, consumer);
        sendMessage(MessageDto.builder().setCommand(Command.SUBSCRIBE).setQueue(queue).build());
    }

    protected void sendMessage(MessageDto dto) {
        sendMessage(dto.toJson());
    }

    public Producer.QueueBuilder producer() {
        return Producer.builder(this, objectMapper);
    }

    public Consumer.SubscribeBuilder consumer() {
        return Consumer.builder(this, objectMapper);
    }

    protected ConsumerManager getConsumerManager() {
        return consumerManager;
    }

    protected static UriBuilder builder() {
        return new Connector().new Builder();
    }

    public class Builder implements UriBuilder, FinalBuilder {

        private Builder() {}

        public FinalBuilder onUri(URI uri) {
            Connector.this.onUri(uri);
            return this;
        }

        public Connector connect() {
            Connector.this.connect();
            return Connector.this;
        }
    }

    public interface UriBuilder {
        FinalBuilder onUri(URI uri);
    }

    public interface FinalBuilder {
        Connector connect();
    }
}