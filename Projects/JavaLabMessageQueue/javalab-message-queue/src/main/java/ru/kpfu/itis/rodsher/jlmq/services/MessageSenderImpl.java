package ru.kpfu.itis.rodsher.jlmq.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.kpfu.itis.rodsher.jlmq.dto.Dto;
import ru.kpfu.itis.rodsher.jlmq.dto.MessageDto;
import ru.kpfu.itis.rodsher.jlmq.dto.Status;
import ru.kpfu.itis.rodsher.jlmq.dto.WebDto;

import java.io.IOException;

@Component
@Profile("standard")
public class MessageSenderImpl implements MessageSender {

    @Override
    public Dto sendMessage(MessageDto messageDto, WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage(messageDto.toJson()));
            return new WebDto(Status.MESSAGE_SEND_SUCCESS);
        } catch (IOException e) {
            return new WebDto(Status.MESSAGE_SEND_ERROR);
        }
    }
}
