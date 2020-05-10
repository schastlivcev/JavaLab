package ru.kpfu.itis.rodsher.jlmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StompConnector {
    private final ObjectMapper objectMapper;
    private final ConsumerManager consumerManager;
    private final InfiniteThread infiniteThread;

    private URI uri;
    private WebSocketStompClient client;
    private StompSession session;

    private StompConnector() {
        objectMapper = new ObjectMapper();
        consumerManager = new ConsumerManager();
        infiniteThread = new InfiniteThread();

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(webSocketClient));
        SockJsClient sockJsClient = new SockJsClient(transports);
        client = new WebSocketStompClient(sockJsClient);
        client.setMessageConverter(new StompJsonMessageConverter(objectMapper, new MappingJackson2MessageConverter()));
    }

    protected void onUri(URI uri) {
        this.uri = uri;
    }

    protected void connect() {
        try {
            session = client.connect(uri.toString(), new StompSessionFrameHandler()).get();
            infiniteThread.start();
        } catch (ResourceAccessException | InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Failed to connect to server on URI: " + uri);
        }
    }

    public void disconnect() {
        infiniteThread.interrupt();
        if(client.isRunning()) {
            client.stop();
        }
    }

    protected StompSession getSession() {
        return session;
    }

    protected void subscribeConsumer(String queue, Consumer consumer) {
        session.subscribe("/topic/" + queue, new SubscribeStompFrameHandler(this));
        consumerManager.addConsumer(queue, consumer);
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
        return new StompConnector().new Builder();
    }

    public class Builder implements UriBuilder, FinalBuilder {

        private Builder() {}

        public FinalBuilder onUri(URI uri) {
            StompConnector.this.onUri(uri);
            return this;
        }

        public StompConnector connect() {
            StompConnector.this.connect();
            return StompConnector.this;
        }
    }

    public interface UriBuilder {
        FinalBuilder onUri(URI uri);
    }

    public interface FinalBuilder {
        StompConnector connect();
    }
}