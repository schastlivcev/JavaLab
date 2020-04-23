package ru.kpfu.itis.rodsher.websockets.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.kpfu.itis.rodsher.websockets.handlers.WebSocketAuthorizationHandshakeHandler;
import ru.kpfu.itis.rodsher.websockets.handlers.WebSocketMessageHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketMessageHandler messageHandler;

    @Autowired
    private WebSocketAuthorizationHandshakeHandler handshakeHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(messageHandler, "/messages")
                .setHandshakeHandler(handshakeHandler).withSockJS();
    }
}