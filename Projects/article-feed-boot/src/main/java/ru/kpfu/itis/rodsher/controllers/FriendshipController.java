package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.Friends;
import ru.kpfu.itis.rodsher.models.FriendsStatus;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.UserService;

@Controller
public class FriendshipController {
    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/friendship")
    public ResponseEntity<String> updateFriendship(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @RequestParam(name = "friend_id") Long friendId,
                                                   @RequestParam(name = "status") String status) {
        Dto dto = userService.checkFriendship(userDetails.getId(), friendId);
        Friends friendship = null;
        if(dto.getStatus().equals(Status.FRIENDSHIP_PRESENTED)) {
            friendship = (Friends) dto.get("friends");
        }
        if(status.equals("DELETE") || status.equals("CANCEL")) {
            userService.removeFriendship(friendship.getUserSender().getId(), friendship.getUserRecipient().getId());
        } else if(status.equals("ACCEPT")) {
            userService.updateFriendship(friendship.getUserSender().getId(), friendship.getUserRecipient().getId(), FriendsStatus.ACCEPTED);
        } else if(status.equals("ADD")) {
            userService.updateFriendship(userDetails.getUser().getId(), friendId, FriendsStatus.REQUESTED);
        } else {
            return new ResponseEntity<>("received", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("received", HttpStatus.OK);
    }
}