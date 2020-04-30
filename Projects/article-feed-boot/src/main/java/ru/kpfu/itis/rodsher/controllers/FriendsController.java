package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.Friends;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.UserService;

import java.util.List;

@Controller
public class FriendsController {
    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/friends")
    public String getFriends(@AuthenticationPrincipal UserDetailsImpl userDetails, ModelMap map) {
        Dto dto = userService.loadFriends(userDetails.getId());
        map.put("friends",(List<Friends>) dto.get("friends"));
        map.put("me", userDetails.getUser());
        return "main/friends";
    }
}