package ru.kpfu.itis.rodsher.jlmq.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.jlmq.dto.Dto;
import ru.kpfu.itis.rodsher.jlmq.dto.MessageDto;
import ru.kpfu.itis.rodsher.jlmq.dto.Status;
import ru.kpfu.itis.rodsher.jlmq.dto.WebDto;
import ru.kpfu.itis.rodsher.jlmq.models.Message;
import ru.kpfu.itis.rodsher.jlmq.models.MessageStatus;
import ru.kpfu.itis.rodsher.jlmq.models.Queue;
import ru.kpfu.itis.rodsher.jlmq.repositories.MessagesRepository;
import ru.kpfu.itis.rodsher.jlmq.repositories.QueuesRepository;

import java.util.Collections;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private QueuesRepository queuesRepository;

    @Override
    public Dto saveMessage(String queueName, String payload) {
        Optional<Queue> queueOptional = queuesRepository.findByName(queueName);
        if(queueOptional.isPresent()) {
            messagesRepository.save(Message.builder().queue(queueOptional.get()).status(MessageStatus.CREATED)
                    .payload(payload).build());
            return new WebDto(Status.MESSAGE_PRODUCE_SUCCESS);
        }
        return new WebDto(Status.MESSAGE_PRODUCE_NO_QUEUE_ERROR);
    }

    @Override
    public Dto updateMessageStatus(Long messageId, MessageStatus status) {
        if(messagesRepository.updateStatusById(messageId, status) > 0) {
            return new WebDto(Status.MESSAGE_UPDATE_STATUS_SUCCESS);
        }
        return new WebDto(Status.MESSAGE_UPDATE_STATUS_ERROR);
    }

    @Override
    public Dto findAvailableMessageFor(String queueName) {
        Optional<Queue> queueOptional = queuesRepository.findByName(queueName);
        if(queueOptional.isPresent()) {
            Optional<Message> messageOptional = messagesRepository
                    .findFirstByQueueAndStatusInOrderByCreatedAtAsc(
                            queueOptional.get(), Collections.singleton(MessageStatus.CREATED));
            if(messageOptional.isPresent()) {
                return new WebDto(Status.MESSAGE_FIND_AVAILABLE_SUCCESS, "message", messageOptional.get());
            }
            return new WebDto(Status.MESSAGE_FIND_AVAILABLE_NO_ERROR);
        }
        return new WebDto(Status.MESSAGE_FIND_AVAILABLE_NO_QUEUE_ERROR);
    }

    @Override
    public Dto findMessageById(Long messageId) {
        Optional<Message> message = messagesRepository.findById(messageId);
        if(message.isPresent()) {
            return new WebDto(Status.MESSAGE_FIND_BY_ID_SUCCESS, "message", message.get());
        }
        return new WebDto(Status.MESSAGE_FIND_BY_ID_NO_ERROR);
    }
}