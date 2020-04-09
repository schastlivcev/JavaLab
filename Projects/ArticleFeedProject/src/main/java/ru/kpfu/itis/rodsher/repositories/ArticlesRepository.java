package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.Article;

import java.util.List;
import java.util.Optional;

public interface ArticlesRepository {
    Long save(Article article);
    List<Article> findByUserId(Long userId);
    Optional<Article> findById(Long id);
    boolean remove(Long id);
}
