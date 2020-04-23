package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.Channel;
import ru.kpfu.itis.rodsher.models.ChannelsToUsers;
import ru.kpfu.itis.rodsher.models.Message;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.services.ChatService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/chat")
    public String getChatPage(@RequestParam("r") Long channelId, HttpSession session, ModelMap map) {
        User me = (User) session.getAttribute("user");
        map.put("user", me);

        Channel channel = (Channel) chatService.getChannel(channelId).get("channel");
        boolean exists = false;
        for(ChannelsToUsers user : channel.getUsers()) {
            if(user.getUser().getId().equals(me.getId())) {
                exists = true;
                break;
            }
        }
        if(!exists) {
            chatService.addUsersToChannel(channelId, me.getId());
            channel = (Channel) chatService.getChannel(channelId).get("channel");
        }
        map.put("channel", channel);

        List<Message> messages = (List<Message>) chatService.loadMessagesForChannel(channelId).get("messages");
        map.put("messages", messages);

        return "chat/chat";
    }
}
