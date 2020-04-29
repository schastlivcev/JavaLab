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
import ru.kpfu.itis.rodsher.models.Channel;
import ru.kpfu.itis.rodsher.models.ChannelsToUsers;
import ru.kpfu.itis.rodsher.models.Message;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.ChatService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chat")
    public String getChat(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "ch", required = false) Long channelId, ModelMap map) {
        if(channelId != null) {
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
        Dto dto = chatService.loadLastChannelMessagesForUserId(userDetails.getId());
        if(!dto.getStatus().equals(Status.MESSAGE_LOAD_LAST_FOR_USER_ID_CHANNELS_SUCCESS)) {
            map.put("error", true);
            return "main/chat_browser";
        }
        List<Message> messages = (List<Message>) dto.get("messages");
        map.put("messages", messages);
        Map<Long, List<User>> channelUsers = new HashMap<>();
        for(Message message : messages) {
            Long chId = message.getChannel().getId();
            List<User> users = (List<User>) chatService.getUsersForChannel(chId).get("users");
            channelUsers.put(chId, users);
        }
        map.put("channelUsers", channelUsers);
        map.put("me", userDetails.getUser());
        return "main/chat_browser";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{user-id}/createСhat")
    public String createChat(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("user-id") Long userId, ModelMap map) {

        Dto dto = chatService.checkIfChannelExistsForUsers(userDetails.getId(), userId);
        if(dto.getStatus().equals(Status.CHANNEL_EXISTS_FOR_USERS)) {
            List<Channel> channels = (List<Channel>) dto.get("channels");
            return "redirect:/chat?ch=" + channels.get(0).getId();
        }
        dto = chatService.createChannel(null);
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