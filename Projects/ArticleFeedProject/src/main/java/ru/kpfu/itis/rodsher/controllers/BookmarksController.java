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
public class BookmarksController {
    @Autowired
    private WallArticleService wallArticleService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/bookmarks")
    public String getBookmarks(@AuthenticationPrincipal UserDetailsImpl userDetails, ModelMap map) {
        map.put("me", userDetails.getUser());
        Dto dto = wallArticleService.loadArticlesByUserIdBookmarks(userDetails.getId());
        if(!dto.getStatus().equals(Status.ARTICLE_LOAD_BY_USER_ID_BOOKMARKS_SUCCESS)) {
            map.put("error", true);
            return "main/bookmarks";
        }
        List<Wall> bookmarks = (List<Wall>) dto.get("walls");
        map.put("bookmarks", bookmarks);
        dto = wallArticleService.loadArticlesByUserIdReplies(userDetails.getId());
        if(!dto.getStatus().equals(Status.ARTICLE_LOAD_BY_USER_ID_REPLIES_SUCCESS)) {
            map.put("error", true);
            return "main/bookmarks";
        }
        List<Wall> replies = (List<Wall>) dto.get("walls");
        List<Long> repliesId = new ArrayList<>();
        for(Wall reply : replies) {
            repliesId.add(reply.getArticle().getId());
        }
        map.put("repliesId", repliesId);
        return "main/bookmarks";
    }
}