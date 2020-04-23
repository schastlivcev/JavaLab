package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.Channel;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.services.ChatService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class RoomsController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/rooms")
    public String getRoomsPage(HttpSession session, ModelMap map) {
        Dto dto = chatService.getAllChannels();
        if(dto.getStatus().equals(Status.CHANNEL_LOAD_ALL_SUCCESS)) {
            List<Channel> channels = (List<Channel>) dto.get("channels");
            map.put("channels", chatService.getAllChannels().get("channels"));
        }
        map.put("user", session.getAttribute("user"));
        return "chat/rooms";
    }

    @GetMapping("/rooms/create")
    public String getCreationPage() {
        return "chat/room_create";
    }

    @PostMapping("/rooms/create")
    public String createRoom(HttpSession session, @RequestParam("name") String roomName) {
        Dto dto = chatService.createChannel(roomName.trim().equals("") ? null : roomName);
        if(dto.getStatus().equals(Status.CHANNEL_ADD_SUCCESS)) {
            Long channelId = (Long) dto.get("channel_id");
            dto = chatService.addUsersToChannel(channelId, ((User) session.getAttribute("user")).getId());
        }
        return "redirect:/rooms";
    }
}