package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.Channel;
import ru.kpfu.itis.rodsher.models.Friends;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.models.Wall;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.ChatService;
import ru.kpfu.itis.rodsher.services.UserService;
import ru.kpfu.itis.rodsher.services.WallArticleService;

import java.util.List;

@Controller
public class UserPageController {
    @Autowired
    private UserService userService;

    @Autowired
    private WallArticleService wallArticleService;

    @Autowired
    private ChatService chatService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public String getPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "redirect:/user/" + userDetails.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{user-id}")
    public String getPage(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("user-id") String userId, ModelMap map) {
        try {
            long id = Long.parseLong(userId);
            if(userDetails.getId() == id) {
                map.put("user", userDetails.getUser());
            } else {
                Dto dto = userService.loadUser(id);
                if(!dto.getStatus().equals(Status.USER_LOAD_SUCCESS)) {
                    map.put("status", 404);
                    map.put("text", "Извините, пользователя с таким id не существует.");
                    return "error_page";
                }
                map.put("user", (User) dto.get("user"));
                dto = userService.checkFriendship(userDetails.getId(), id);
                if(dto.getStatus().equals(Status.FRIENDSHIP_PRESENTED)) {
                    map.put("friends", (Friends) dto.get("friends"));
                }
                dto = chatService.checkIfChannelExistsForUsers(userDetails.getId(), id);
                if(dto.getStatus().equals(Status.CHANNEL_EXISTS_FOR_USERS)) {
                    List<Channel> channels = (List<Channel>) dto.get("channels");
                    map.put("channelId", channels.get(0).getId());
                }
            }
            map.put("me", userDetails.getUser());
            Dto dto = wallArticleService.loadArticlesByUserId(id);
            if(!dto.getStatus().equals(Status.ARTICLE_LOAD_BY_USER_ID_SUCCESS)) {
                map.put("status", 505);
                map.put("text", "Ошибка при загрузке страницы");
                return "error_page";
            }
            map.put("walls", (List<Wall>) dto.get("walls"));
            return "main/user_page";
        } catch(NumberFormatException e) {
            map.put("status", 404);
            map.put("text", "Извините, такой страницы не существует. Некорректный id.");
            return "error_page";
        }
    }
}