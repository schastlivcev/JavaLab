package ru.kpfu.itis.rodsher.jlmq.websocket.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.kpfu.itis.rodsher.jlmq.dto.Dto;
import ru.kpfu.itis.rodsher.jlmq.dto.MessageDto;
import ru.kpfu.itis.rodsher.jlmq.services.ConsumerService;
import ru.kpfu.itis.rodsher.jlmq.services.MessageService;

@Component
@Profile("standard")
public class WebSocketMessageHandler extends TextWebSocketHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConsumerService consumerService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Con[id=" + session.getId() + "] opened.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.print("Con[id=" + session.getId() + "] received Message[" + message.getPayload() + "]");
        try {
            MessageDto messageDto = MessageDto.fromJson(message.getPayload());
            if(messageDto.getCommand() != null) {
                switch(messageDto.getCommand()) {
                    case SEND:
                        handleProduceMessage(session, messageDto);
                        return;
                    case SUBSCRIBE:
                        handleSubscribeMessage(session, messageDto);
                        return;
                    case ACCEPT:
                        handleAcceptMessage(session, messageDto);
                        return;
                    case COMPLETE:
                        handleCompleteMessage(session, messageDto);
                        return;
                }
            }
            System.out.println(" which has nothing to do with itself.");
        } catch (IllegalArgumentException e) {
            System.out.println(" which has unacceptable format!");
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Con[id=" + session.getId() + "] experienced "
                + exception.getClass().getSimpleName() + "[" + exception.getMessage() + "]");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        consumerService.removeConsumersBySession(session);
        System.out.println("Con[id=" + session.getId() + "] closed due to " + status);
    }

    private void handleProduceMessage(WebSocketSession session, MessageDto messageDto) {
        System.out.print(" to add");
        if(messageDto.getQueue() != null) {
            if(messageDto.getPayload() != null) {
                Dto dto = messageService.saveMessage(messageDto.getQueue(), messageDto.getPayload().toString());
                switch (dto.getStatus()) {
                    case MESSAGE_PRODUCE_SUCCESS:
                        System.out.println(", successfully added.");
                        sendMessageToConsumer(session, messageDto.getQueue());
                        return;
                    case MESSAGE_PRODUCE_NO_QUEUE_ERROR:
                        System.out.println(", but such queue doesn't exist!");
                        return;
                    case MESSAGE_PRODUCE_PAYLOAD_PARSE_ERROR:
                        System.out.println(", but failed to parse payload.");
                        return;
                }
            }
            System.out.println(", but it doesn't contain any payload!");
            return;
        }
        System.out.println(", but it doesn't contain Queue name!");
    }

    private void handleSubscribeMessage(WebSocketSession session, MessageDto messageDto) {
        System.out.print(" to subscribe");
        if(messageDto.getQueue() != null) {
            Dto dto = consumerService.subscribeConsumer(messageDto.getQueue(), session);
            switch (dto.getStatus()) {
                case CONSUMER_SUBSCRIBE_SUCCESS:
                    System.out.println(", successfully subscribed.");
                    sendMessageToConsumer(session, messageDto.getQueue());;
                    return;
                case CONSUMER_SUBSCRIBE_NO_QUEUE_ERROR:
                    System.out.println(", but such Queue doesn't exist!");
                    return;
                case CONSUMER_SUBSCRIBE_ALREADY_EXISTS_ERROR:
                    System.out.println(", but Consumer for such queue already exists!");
                    return;
            }
        }
        System.out.println(", but it doesn't contain Queue name!");
    }

    private void handleAcceptMessage(WebSocketSession session, MessageDto messageDto) {
        System.out.print(" to accept");
        if(messageDto.getMessageId() != null) {
            Dto dto = consumerService.acceptMessage(messageDto.getMessageId());
            switch (dto.getStatus()) {
                case CONSUMER_ACCEPT_MESSAGE_SUCCESS:
                    System.out.println(", accepted by Consumer successfully.");
                    return;
                case CONSUMER_ACCEPT_MESSAGE_NO_MESSAGE_ERROR:
                    System.out.println(", failed to accept due to message doesn't exist!");
                    return;
            }
        }
        System.out.println(", but it doesn't contain Message id!");
    }

    private void handleCompleteMessage(WebSocketSession session, MessageDto messageDto) {
        System.out.print(" to complete");
        if(messageDto.getMessageId() != null) {
            Dto dto = consumerService.completeMessage(messageDto.getMessageId());
            switch (dto.getStatus()) {
                case CONSUMER_COMPLETE_MESSAGE_SUCCESS:
                    System.out.println(", completed by Consumer successfully.");
                    sendMessageToConsumer(session, (String) dto.get("queue"));
                    return;
                case CONSUMER_COMPLETE_MESSAGE_NO_MESSAGE_ERROR:
                    System.out.println(", failed to complete due to message doesn't exist!");
                    return;
            }
        }
        System.out.println(", but it doesn't contain Message id!");
    }

    private void sendMessageToConsumer(WebSocketSession session, String queue) {
        Dto dto = consumerService.sendMessageForConsumerIfAvailableOn(queue);
        switch (dto.getStatus()) {
            case MESSAGE_SEND_TO_CONSUMER_SUCCESS:
                System.out.println("Message with Task was sent to Consumer of Queue["
                        + queue + "] successfully.");
                return;
            case MESSAGE_SEND_TO_CONSUMER_NO_CONSUMER_ERROR:
                System.out.println("Message with Task was tried to send, but there is no Consumer for Queue["
                        + queue + "].");
                return;
            case MESSAGE_SEND_TO_CONSUMER_UNAVAILABLE_ERROR:
                System.out.println("Message with Task was tried to send, but Consumer of Queue["
                        + queue + "] is busy.");
                return;
            case MESSAGE_SEND_TO_CONSUMER_NO_MESSAGE_ERROR:
                System.out.println("Message with Task was tried to send to Consumer of Queue["
                        + queue + "], but such Message no longer exists!");
                return;
            case MESSAGE_SEND_TO_CONSUMER_SEND_ERROR:
                System.out.println("Message with Task was tried to send to Consumer of Queue["
                        + queue + "], but Transport Error occurred!");
                return;
        }
    }
}