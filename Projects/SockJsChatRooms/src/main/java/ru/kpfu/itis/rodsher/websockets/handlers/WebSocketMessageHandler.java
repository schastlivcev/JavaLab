package ru.kpfu.itis.rodsher.websockets.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.Message;
import ru.kpfu.itis.rodsher.services.ChatService;

import java.io.IOException;
import java.util.*;

@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {

    private static final Map<Long, List<WebSocketSession>> channels = new HashMap<>();

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> messageWS) throws Exception {
        String messageWSPayload = (String) messageWS.getPayload();
        Map<String, String> message = objectMapper.readValue(messageWSPayload, Map.class);

        Long channelId = Long.parseLong(message.get("channel_id"));
        Long authorId = Long.parseLong(message.get("user_id"));

        if(!channels.containsKey(channelId)) {
            List<WebSocketSession> sessionList = new ArrayList<>();
            sessionList.add(session);
            channels.put(channelId, sessionList);
        } else if(!channels.get(channelId).contains(session)) {
            channels.get(channelId).add(session);
        }

        saveAndSend(channelId, authorId, message.get("content"));
    }

    private void saveAndSend(Long channelId, Long authorId, String content) throws IOException {
        Dto dto = chatService.saveMessage(channelId, authorId, content);
        Message messageFromDb = (Message) chatService.getMessage((Long) dto.get("message_id")).get("message");

        String sendingMessage = objectMapper.writeValueAsString(Collections.singletonMap("content", createMessageHtml(messageFromDb)));

        for(Map.Entry<Long, List<WebSocketSession>> channel : channels.entrySet()) {
            if(channel.getKey().equals(channelId)) {
                for(WebSocketSession channelSession : channel.getValue()) {
                    System.out.println("Sending to session: " + channelSession.getId() + ", opened: " + channelSession.isOpen() + ", message: " + messageFromDb.getContent());
                    if(channelSession.isOpen()) {
                        channelSession.sendMessage(new TextMessage(sendingMessage));
                    }
                }
                break;
            }
        }
    }

    private String createMessageHtml(Message message) {
        try {
            Configuration configuration = freeMarkerConfig.getConfiguration();
            configuration.setEncoding(Locale.getDefault(), "UTF-8");
            Template template = configuration.getTemplate("chat/message.ftl");
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, Collections.singletonMap("message", message));
        } catch (IOException e) {
            throw new IllegalStateException("template creation exception.", e);
        } catch (TemplateException e) {
            throw new IllegalStateException("template processing exception.", e);
        }
    }
}