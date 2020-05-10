package ru.kpfu.itis.rodsher.jlmq.websocket.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.jlmq.dto.Dto;
import ru.kpfu.itis.rodsher.jlmq.services.ConsumerStompService;

@Component
@Profile("stomp")
public class InboundStompChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private ConsumerStompService consumerService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        switch (accessor.getCommand()) {
            case CONNECT:
                System.out.println("Con[id=" + accessor.getSessionId() + "] opened.");
                return message;
            case SUBSCRIBE:
                handleSubscribeMessage(accessor);
                return message;
            case ACK:
                String type = accessor.getFirstNativeHeader("type");
                if(type != null) {
                    switch (type) {
                        case "ACCEPT":
                            handleAcceptMessage(accessor);
                            return null;
                        case "COMPLETE":
                            handleCompleteMessage(accessor);
                            return null;
                    }
                }
                System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[messageId=" + accessor.getMessageId() + "] to acknowledge, but it doesn't contain acknowledge type!");
                return null;
            case NACK:
                System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[messageId=" + accessor.getMessageId() + "] to unconsume.");
                return null;
            case DISCONNECT:
                System.out.println("Con[id=" + accessor.getSessionId() + "] closed.");
                consumerService.removeConsumersBySession(accessor.getSessionId());
                return message;
            default:
                return message;
        }
    }

    private void handleSubscribeMessage(StompHeaderAccessor accessor) {
        String queue = null;
        try {
            queue = accessor.getDestination().substring("/topic/".length());
        } catch (NullPointerException | StringIndexOutOfBoundsException e) {
            System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[queue=] to subscribe, but it doesn't contain Queue name!");
            return;
        }
        Dto dto = consumerService.subscribeConsumer(queue, accessor.getSessionId());
        switch (dto.getStatus()) {
            case CONSUMER_SUBSCRIBE_SUCCESS:
                System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[queue=" + queue + "] to subscribe, successfully subscribed.");
                return;
            case CONSUMER_SUBSCRIBE_NO_QUEUE_ERROR:
                System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[queue=" + queue + "] to subscribe, but such Queue doesn't exist!");
                return;
            case CONSUMER_SUBSCRIBE_ALREADY_EXISTS_ERROR:
                System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[queue=" + queue + "] to subscribe, but Consumer for such queue already exists!");
                return;
        }
    }

    private void handleAcceptMessage(StompHeaderAccessor accessor) {
        if(accessor.getMessageId() != null) {
            Dto dto = consumerService.acceptMessage(Long.parseLong(accessor.getMessageId()));
            switch (dto.getStatus()) {
                case CONSUMER_ACCEPT_MESSAGE_SUCCESS:
                    System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[messageId=" + accessor.getMessageId() + "] to acknowledge about acceptance, accepted by Consumer successfully.");
                    return;
                case CONSUMER_ACCEPT_MESSAGE_NO_MESSAGE_ERROR:
                    System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[messageId=" + accessor.getMessageId() + "] to acknowledge about acceptance, but failed to accept due to message doesn't exist!");
                    return;
            }
        }
        System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[messageId=" + accessor.getMessageId() + "] to acknowledge about acceptance, but it doesn't contain Message id!");
    }

    private void handleCompleteMessage(StompHeaderAccessor accessor) {
        if(accessor.getMessageId() != null) {
            Dto dto = consumerService.completeMessage(Long.parseLong(accessor.getMessageId()));
            switch (dto.getStatus()) {
                case CONSUMER_COMPLETE_MESSAGE_SUCCESS:
                    System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[messageId=" + accessor.getMessageId() + "] to acknowledge about completion, completed by Consumer successfully.");
                    return;
                case CONSUMER_COMPLETE_MESSAGE_NO_MESSAGE_ERROR:
                    System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[messageId=" + accessor.getMessageId() + "] to acknowledge about completion, failed to complete due to message doesn't exist!");
                    return;
            }
        }
        System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[messageId=" + accessor.getMessageId() + "] to acknowledge about completion, but it doesn't contain Message id!");
    }
}