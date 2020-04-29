package ru.kpfu.itis.rodsher.services;

import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.Article;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.models.Wall;

public interface WallArticleService {
    Dto addArticle(Article article);
    Dto loadArticle(Long id);
    Dto loadArticlesByUserIdReplies(Long userId);
    Dto loadArticlesByUserIdBookmarks(Long userId);
    Dto loadArticlesByUserIdWithReplies(Long userId);
    Dto loadArticlesByUserIdAndArticleId(Long userId, Long articleId);
    Dto loadArticlesByUserFriends(Long userId);
    Dto deleteArticle(Long id);
    Dto updateWall(Wall wall, String type, User user);
}
