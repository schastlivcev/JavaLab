package ru.kpfu.itis.rodsher.jlmq.services;

import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.util.Pair;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.jlmq.dto.*;
import ru.kpfu.itis.rodsher.jlmq.models.Message;
import ru.kpfu.itis.rodsher.jlmq.models.MessageStatus;
import ru.kpfu.itis.rodsher.jlmq.models.Queue;
import ru.kpfu.itis.rodsher.jlmq.repositories.QueuesRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@Profile("stomp")
public class ConsumerStompServiceImpl implements ConsumerStompService {
    private static final Map<String, Pair<String, Optional<Message>>> consumers = new HashMap<>();

    @Autowired
    private QueuesRepository queuesRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public Dto subscribeConsumer(String queue, String sessionId) {
        if(consumers.get(queue) != null) {
            return new WebDto(Status.CONSUMER_SUBSCRIBE_ALREADY_EXISTS_ERROR);
        }
        Optional<Queue> queueOptional = queuesRepository.findByName(queue);
        if(queueOptional.isPresent()) {
            consumers.put(queue, Pair.of(sessionId, Optional.empty()));
            return new WebDto(Status.CONSUMER_SUBSCRIBE_SUCCESS);
        }
        return new WebDto(Status.CONSUMER_SUBSCRIBE_NO_QUEUE_ERROR);
    }

    @Override
    public Dto sendMessageForConsumerIfAvailableOn(String queue) {
        if(consumers.get(queue) != null) {
            if(isConsumerAvailable(queue)) {
                Dto dto = messageService.findAvailableMessageFor(queue);
                if(dto.getStatus().equals(Status.MESSAGE_FIND_AVAILABLE_SUCCESS)) {
                    Message message = (Message) dto.get("message");
                    setConsumerMessage(queue, message);
                    SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
                    accessor.setSessionId(consumers.get(queue).getFirst());
                    accessor.setNativeHeader("message-id", message.getId().toString());
                    accessor.getMessageHeaders();
                    messagingTemplate.convertAndSend("/topic/" + queue, message.getPayload(), accessor.getMessageHeaders());
                    return new WebDto(Status.MESSAGE_SEND_TO_CONSUMER_SUCCESS);
                }
                return new WebDto(Status.MESSAGE_SEND_TO_CONSUMER_NO_MESSAGE_ERROR);
            }
            return new WebDto(Status.MESSAGE_SEND_TO_CONSUMER_UNAVAILABLE_ERROR);
        }
        return new WebDto(Status.MESSAGE_SEND_TO_CONSUMER_NO_CONSUMER_ERROR);
    }

    @Override
    public Dto completeMessage(Long messageId) {
        Dto dto = messageService.updateMessageStatus(messageId, MessageStatus.COMPLETED);
        if(dto.getStatus().equals(Status.MESSAGE_UPDATE_STATUS_SUCCESS)) {
            dto = messageService.findMessageById(messageId);
            if(dto.getStatus().equals(Status.MESSAGE_FIND_BY_ID_SUCCESS)) {
                Message message = (Message) dto.get("message");
                setConsumerMessage(message.getQueue().getName(), null);
                return new WebDto(Status.CONSUMER_COMPLETE_MESSAGE_SUCCESS, "queue", message.getQueue().getName());
            }
        }
        return new WebDto(Status.CONSUMER_COMPLETE_MESSAGE_NO_MESSAGE_ERROR);
    }

    @Override
    @Synchronized
    public Dto acceptMessage(Long messageId) {
        Dto dto = messageService.updateMessageStatus(messageId, MessageStatus.ACCEPTED);
        if(dto.getStatus().equals(Status.MESSAGE_UPDATE_STATUS_SUCCESS)) {
            dto = messageService.findMessageById(messageId);
            if(dto.getStatus().equals(Status.MESSAGE_FIND_BY_ID_SUCCESS)) {
                Message message = (Message) dto.get("message");
                setConsumerMessage(message.getQueue().getName(), message);
                return new WebDto(Status.CONSUMER_ACCEPT_MESSAGE_SUCCESS);
            }
        }
        return new WebDto(Status.CONSUMER_ACCEPT_MESSAGE_NO_MESSAGE_ERROR);
    }

    @Override
    public void removeConsumersBySession(String sessionId) {
        for(Map.Entry<String, Pair<String, Optional<Message>>> consumer : consumers.entrySet()) {
            if(consumer.getValue().getFirst().equals(sessionId)) {
                if(consumer.getValue().getSecond().isPresent()) {
                    messageService.updateMessageStatus(consumer.getValue().getSecond().get().getId(),
                            MessageStatus.CREATED);
                }
                consumers.remove(consumer.getKey());
            }
        }
    }

    @Override
    public Set<String> getQueues() {
        return consumers.keySet();
    }

    private boolean isConsumerAvailable(String queue) {
        return !consumers.get(queue).getSecond().isPresent();
    }

    private void setConsumerMessage(String queue, Message message) {
        consumers.put(queue, Pair.of(consumers.get(queue).getFirst(), Optional.ofNullable(message)));
    }
}
