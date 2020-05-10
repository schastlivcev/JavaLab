package ru.kpfu.itis.rodsher.jlmq.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.*;
import ru.kpfu.itis.rodsher.jlmq.websocket.handlers.InboundStompChannelInterceptor;
import ru.kpfu.itis.rodsher.jlmq.websocket.handlers.StompJsonMessageConverter;

import java.util.List;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
@EnableScheduling
@Profile("stomp")
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private InboundStompChannelInterceptor channelInterceptor;

    @Autowired
    private StompJsonMessageConverter messageConverter;

    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/jlmq").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(messageConverter);
        return false;
    }
}
