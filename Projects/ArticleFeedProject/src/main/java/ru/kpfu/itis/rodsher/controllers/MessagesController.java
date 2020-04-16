package ru.kpfu.itis.rodsher.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.Channel;
import ru.kpfu.itis.rodsher.models.Message;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.ChatService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
public class MessagesController {
    private static final Map<Pair<String, Channel>, List<Message>> map = new HashMap<>();

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/messages")
    public ResponseEntity<Object> receiveMessage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @RequestParam("ch") Long channelId, @RequestBody String jsonMessage) {
        try {
            Map response = objectMapper.readValue(jsonMessage, Map.class);

            Dto channelDto = chatService.getChannel(channelId);
            if(!channelDto.getStatus().equals(Status.CHANNEL_LOAD_SUCCESS) || response.get("content") == null
                    || ((String) response.get("content")).trim().equals("") || response.get("page_id") == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Channel channel = (Channel) channelDto.get("channel");
            String pageId = (String) response.get("page_id");

            Dto messageDto = chatService.saveMessage(channelId, userDetails.getId(), (String) response.get("content"));
            if(!messageDto.getStatus().equals(Status.MESSAGE_ADD_SUCCESS)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Message message = (Message) chatService.getMessage((Long) messageDto.get("message_id")).get("message");

            // LONG POLLING
            for(Pair<String, Channel> pair : map.keySet()) {
                if(pair.getSecond().equals(channel)) {
                    synchronized (map.get(pair)) {
                        map.get(pair).add(message);
                        map.get(pair).notify();
                    }
                }
            }

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (IOException e) {
            throw new IllegalArgumentException("Unacceptable received message.");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/messages")
    public ResponseEntity<String> getMessages(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @RequestParam("ch") Long channelId,
                                              @RequestParam("page_id") String pageId,
                                              HttpServletRequest request) {
        Dto dto = chatService.getChannel(channelId);
        if(!dto.getStatus().equals(Status.CHANNEL_LOAD_SUCCESS)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Channel channel = (Channel) dto.get("channel");

        if(!map.containsKey(Pair.of(pageId, channel))) {
            map.put(Pair.of(pageId, channel), new ArrayList<>());
        }

        synchronized (map.get(Pair.of(pageId, channel))) {
            if(map.get(Pair.of(pageId, channel)).isEmpty()) {
                try {
                    map.get(Pair.of(pageId, channel)).wait();
                } catch (InterruptedException e) {
                    throw new IllegalStateException("Channel messages wait exception.", e);
                }
            }
        }

        List<Message> messages = map.get(Pair.of(pageId, channel));
        String messagesHtml = createMessagesHtml(messages, request);

        map.get(Pair.of(pageId, channel)).clear();
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "text/html;charset=UTF-8");
        return new ResponseEntity<>(messagesHtml, headers, HttpStatus.OK);
    }

    private String createMessagesHtml(List<Message> messages, HttpServletRequest request) {
        try {
            Configuration configuration = freeMarkerConfig.getConfiguration();
            configuration.setEncoding(Locale.getDefault(), "UTF-8");
            configuration.setSharedVariable("rc", new RequestContext(request));
            Template template = configuration.getTemplate("templates/message.ftl");
            StringBuilder messagesHtml = new StringBuilder();
            for(Message message : messages) {
                messagesHtml.append(FreeMarkerTemplateUtils.processTemplateIntoString(template, Collections.singletonMap("message", message)));
            }
            return messagesHtml.toString();
        } catch (IOException e) {
            throw new IllegalStateException("template creation exception.", e);
        } catch (TemplateException e) {
            throw new IllegalStateException("template processing exception.", e);
        }
    }
}