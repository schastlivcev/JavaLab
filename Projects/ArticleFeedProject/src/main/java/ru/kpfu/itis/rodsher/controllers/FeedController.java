package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.Wall;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.WallArticleService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    @Autowired
    private WallArticleService wallArticleService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/feed")
    public String getFeed(@AuthenticationPrincipal UserDetailsImpl userDetails, ModelMap map) {
        map.put("me", userDetails.getUser());
        Dto dto = wallArticleService.loadArticlesByUserFriends(userDetails.getId());
        if(!dto.getStatus().equals(Status.ARTICLE_LOAD_BY_USER_FRIENDS_SUCCESS)) {
            map.put("error", true);
            return "main/feed";
        }
        List<Wall> walls = (List<Wall>) dto.get("walls");
        map.put("walls", walls);
        dto = wallArticleService.loadArticlesByUserIdReplies(userDetails.getId());
        if(!dto.getStatus().equals(Status.ARTICLE_LOAD_BY_USER_ID_REPLIES_SUCCESS)) {
            map.put("error", true);
            return "main/feed";
        }
        List<Wall> replies = (List<Wall>) dto.get("walls");
        List<Long> repliesId = new ArrayList<>();
        for(Wall reply : replies) {
            repliesId.add(reply.getArticle().getId());
        }
        map.put("repliesId", repliesId);
        dto = wallArticleService.loadArticlesByUserIdBookmarks(userDetails.getId());
        if(!dto.getStatus().equals(Status.ARTICLE_LOAD_BY_USER_ID_BOOKMARKS_SUCCESS)) {
            map.put("error", true);
            return "main/feed";
        }
        List<Wall> bookmarks = (List<Wall>) dto.get("walls");
        List<Long> bookmarksId = new ArrayList<>();
        for(Wall bookmark : bookmarks) {
            bookmarksId.add(bookmark.getArticle().getId());
        }
        map.put("bookmarksId", bookmarksId);
        return "main/feed";
    }
}
