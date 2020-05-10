package ru.kpfu.itis.rodsher.jlmq.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kpfu.itis.rodsher.jlmq.dto.Dto;
import ru.kpfu.itis.rodsher.jlmq.services.ConsumerStompService;
import ru.kpfu.itis.rodsher.jlmq.services.MessageService;

@Controller
@Profile("stomp")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConsumerStompService consumerStompService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/{queue}")
    public void receiveMessage(@DestinationVariable("queue") String queue, StompHeaderAccessor accessor, @Payload JsonNode payload) {
        if(payload != null) {
            Dto dto = messageService.saveMessage(queue, payload.toString());
            switch (dto.getStatus()) {
                case MESSAGE_PRODUCE_SUCCESS:
                    System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[payload=" + payload.toString() + ", queue=" + queue + "] to add, successfully added.");
//                    sendMessageToConsumer(session, messageDto.getQueue());
                    return;
                case MESSAGE_PRODUCE_NO_QUEUE_ERROR:
                    System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[payload=" + payload.toString() + ", queue=" + queue + "] to add, but such queue doesn't exist!");
                    return;
                case MESSAGE_PRODUCE_PAYLOAD_PARSE_ERROR:
                    System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[payload=" + payload.toString() + ", queue=" + queue + "] to add, but failed to parse payload.");
                    return;
            }
        }
        System.out.println("Con[id=" + accessor.getSessionId() + "] received Message[payload=, queue=" + queue + "] to add, but it doesn't contain any payload!");
    }

    @Scheduled(fixedDelay = 5000)
    public void checkConsumerAvailability() {
        for(String queue : consumerStompService.getQueues()) {
            tryToSendMessageToConsumer(queue);
        }
    }

//    @SubscribeMapping("/{queue}")
//    public String subscribe(@DestinationVariable("queue") String queue, StompHeaderAccessor accessor) {
//        System.out.println("SUBSCRIBE:" + queue);
//        return "subscribed";
//    }

    private void tryToSendMessageToConsumer(String queue) {
        Dto dto = consumerStompService.sendMessageForConsumerIfAvailableOn(queue);
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