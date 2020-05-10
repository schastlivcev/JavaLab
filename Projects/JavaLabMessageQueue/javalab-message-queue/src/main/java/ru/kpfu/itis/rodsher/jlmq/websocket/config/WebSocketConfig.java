package ru.kpfu.itis.rodsher.jlmq.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.kpfu.itis.rodsher.jlmq.websocket.handlers.WebSocketMessageHandler;

@Configuration
@EnableWebSocket
@Profile("standard")
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private WebSocketMessageHandler webSocketMessageHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketMessageHandler, "/jlmq");
    }
}
