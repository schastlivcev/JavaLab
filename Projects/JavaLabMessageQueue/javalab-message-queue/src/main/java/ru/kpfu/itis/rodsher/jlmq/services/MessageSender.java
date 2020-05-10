package ru.kpfu.itis.rodsher.jlmq.services;

import org.springframework.web.socket.WebSocketSession;
import ru.kpfu.itis.rodsher.jlmq.dto.Dto;
import ru.kpfu.itis.rodsher.jlmq.dto.MessageDto;

public interface MessageSender {
    Dto sendMessage(MessageDto messageDto, WebSocketSession session);
}
