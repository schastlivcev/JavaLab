package ru.kpfu.itis.rodsher.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.rodsher.converters.ConvertToArticle;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.Article;
import ru.kpfu.itis.rodsher.models.Wall;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.WallArticleService;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private WallArticleService wallArticleService;

    @Autowired
    private ObjectMapper objectMapper;

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
        System.out.println("HEADING:" + heading + "CONTENT:" + content);
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

//    @PreAuthorize("isAuthenticated()")
//    @DeleteMapping("/article/{article-id}")
//    public ResponseEntity<String> deleteArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                @PathVariable("article-id") Long articleId) {
////        try {
////            long id = Long.parseLong(articleId);
//            Dto dto = wallArticleService.deleteArticle(articleId);
//            if(!dto.getStatus().equals(Status.ARTICLE_DELETE_SUCCESS)) {
//                return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//            return new ResponseEntity<>("removed", HttpStatus.OK);
////        } catch (NumberFormatException e) {
////            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
////        }
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/article/{wall-id}")
    public String getArtilce(@AuthenticationPrincipal UserDetailsImpl userDetails, @ConvertToArticle @PathVariable("wall-id") Wall wall, ModelMap map) {
        if(wall == null) {
            map.put("status", 404);
            map.put("text", "Такой записи не существует.");
            return "error_page";
        }
        map.put("wall", wall);
        Dto dto;
        if(!wall.getUser().getId().equals(userDetails.getId())) {
            dto = wallArticleService.loadArticlesByUserIdAndArticleId(userDetails.getId(), wall.getArticle().getId());
            List<Wall> myWalls = (List<Wall>) dto.get("walls");
            boolean replied = false;
            boolean bookmarked = false;
            for(Wall myWall : myWalls) {
                if(myWall.isReply()) {
                    replied = true;
                }
                if(myWall.isBookmark()) {
                    bookmarked = true;
                }
            }
            map.put("replied", replied);
            map.put("bookmarked", bookmarked);
        }
        map.put("me", userDetails.getUser());
        return "main/article";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/article/{wall-id}")
    public ResponseEntity<String> updateWall(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("type") String type, @PathVariable("wall-id") Long wallId) {
        Dto dto = wallArticleService.loadArticle(wallId);
        if(!dto.getStatus().equals(Status.ARTICLE_LOAD_SUCCESS)) {
            return new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        Wall wall = (Wall) dto.get("wall");
        dto = wallArticleService.updateWall(wall, type, userDetails.getUser());
        switch(dto.getStatus()) {
            case ARTICLE_UPDATE_DELETE_SUCCESS:
                return new ResponseEntity<>("DELETED", HttpStatus.OK);
            case ARTICLE_UPDATE_DELETE_ERROR:
                return new ResponseEntity<>("DELETE_ERROR", HttpStatus.OK);
            case ARTICLE_UPDATE_DELETE_REPLY_SUCCESS:
                return new ResponseEntity<>("REPLY_DELETED", HttpStatus.OK);
            case ARTICLE_UPDATE_DELETE_REPLY_ERROR:
                return new ResponseEntity<>("REPLY_DELETE_ERROR", HttpStatus.OK);
            case ARTICLE_UPDATE_DELETE_BOOKMARK_SUCCESS:
                return new ResponseEntity<>("BOOKMARK_DELETED", HttpStatus.OK);
            case ARTICLE_UPDATE_DELETE_BOOKMARK_ERROR:
                return new ResponseEntity<>("BOOKMARK_DELETE_ERROR", HttpStatus.OK);
            case ARTICLE_UPDATE_BOOKMARK_SUCCESS:
                return new ResponseEntity<>("BOOKMARKED", HttpStatus.OK);
            case ARTICLE_UPDATE_BOOKMARK_ERROR:
                return new ResponseEntity<>("BOOKMARK_ERROR", HttpStatus.OK);
            case ARTICLE_UPDATE_REPLY_SUCCESS:
                return new ResponseEntity<>("REPLIED", HttpStatus.OK);
            case ARTICLE_UPDATE_REPLY_ERROR:
                return new ResponseEntity<>("REPLY_ERROR", HttpStatus.OK);
            default:
                return new ResponseEntity<>("ERROR", HttpStatus.OK);
        }
    }
}