package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.Article;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.WallArticleService;

@Controller
public class ArticleController {
    @Autowired
    private WallArticleService wallArticleService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String getArticleCreationPage(@AuthenticationPrincipal UserDetailsImpl userDetails, ModelMap map) {
        map.put("type", "user");
        map.put("user", userDetails.getUser());
        return "main/add_article";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public ResponseEntity<String> addArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestParam(value = "heading", required = false) String heading,
                                             @RequestBody(required = false) String content) {
        Dto dto = wallArticleService.addArticle(Article.builder()
                .user(userDetails.getUser())
                .heading(heading.trim().equals("") ? null : heading)
                .content(content)
                .build());
        if(dto.getStatus().equals(Status.ARTICLE_ADD_SUCCESS)) {
            return new ResponseEntity<>("added", HttpStatus.OK);
        }
        return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/article/{article-id}")
    public ResponseEntity<String> deleteArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable("article-id") String articleId) {
        try {
            long id = Long.parseLong(articleId);
            Dto dto = wallArticleService.deleteArticle(id);
            if(!dto.getStatus().equals(Status.ARTICLE_DELETE_SUCCESS)) {
                return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("removed", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
