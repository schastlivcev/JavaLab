package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.Message;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.ChatService;

import java.util.List;
import java.util.UUID;

@Controller
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chat")
    public String getChat(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("ch") Long channelId, ModelMap map) {
        List<Message> messages = (List<Message>) chatService.loadMessagesForChannel(channelId).get("messages");
        map.put("messages", messages);

        List<User> users = (List<User>) chatService.getUsersForChannel(channelId).get("users");
        for(User user : users) {
            if(!user.getId().equals(userDetails.getId())) {
                map.put("user", user);
                break;
            }
        }
        map.put("pageId", UUID.randomUUID().toString());
        return "main/chat";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{user-id}/create_chat")
    public String createChat(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("user-id") Long userId, ModelMap map) {
        if(chatService.checkIfChannelExistsForUsers(userDetails.getId(), userId).getStatus()
                .equals(Status.CHANNEL_EXISTS_FOR_USERS)) {
            map.put("status", 505);
            map.put("text", "Чат уже существует.");
            return "error_page";
        }
        Dto dto = chatService.createChannel(null);
        if(!dto.getStatus().equals(Status.CHANNEL_ADD_SUCCESS)) {
            map.put("status", 505);
            map.put("text", "Ошибка при создании чата.");
            return "error_page";
        }
        Long channelId = (Long) dto.get("channel_id");
        chatService.addUsersToChannel(channelId, userDetails.getId(), userId);
        return "redirect:/chat?ch=" + channelId;
    }
}