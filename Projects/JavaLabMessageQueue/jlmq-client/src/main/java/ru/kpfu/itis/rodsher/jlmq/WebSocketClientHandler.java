package ru.kpfu.itis.rodsher.jlmq;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

public class WebSocketClientHandler implements WebSocketHandler {
    private Connector connector;

    protected WebSocketClientHandler(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        System.out.println("Con[id=" + webSocketSession.getId() + "] opened.");
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        System.out.print("Message[" + webSocketMessage.getPayload() + "] ");
        try {
            MessageDto messageDto = MessageDto.fromJson(webSocketMessage.getPayload().toString());
            if(messageDto.getCommand() != null) {
                switch (messageDto.getCommand()) {
                    case RECEIVE:
                        if(messageDto.getQueue() != null) {
                            Optional<Consumer> consumerOptional = connector.getConsumerManager()
                                    .getConsumerFor(messageDto.getQueue());
                            if(consumerOptional.isPresent()) {
                                Consumer consumer = consumerOptional.get();
                                System.out.println("has been given to Consumer[queue=" + messageDto.getQueue() + "].");
                                consumer.receive(messageDto);
                                return;
                            }
                        }
                        System.out.println("has no Consumer for such queue.");
                        return;
                }
            }
            System.out.println("has nothing to do with itself.");
        } catch (IllegalArgumentException e) {
            System.out.println("has unacceptable format!");
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        System.out.println("Con[id=" + webSocketSession.getId() + "]  experienced "
                + throwable.getClass().getSimpleName() + "[" + throwable.getMessage() + "]");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        connector.disconnect();
        System.out.println("Con[id=" + webSocketSession.getId() + "] closed due to " + closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
