package ru.kpfu.itis.rodsher.services;

import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.Article;

public interface WallArticleService {
    Dto addArticle(Article article);
    Dto loadArticle(Long id);
    Dto loadArticlesByUserId(Long userId);
    Dto deleteArticle(Long id);
}
