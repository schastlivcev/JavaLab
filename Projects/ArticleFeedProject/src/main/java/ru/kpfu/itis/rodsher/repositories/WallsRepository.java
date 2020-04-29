package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.Wall;

import java.util.List;
import java.util.Optional;

public interface WallsRepository {
    Long save(Wall wall);
    List<Wall> findByUserIdWithReplies(Long userId);
    List<Wall> findByUserIdBookmarks(Long userId);
    List<Wall> findByUserIdReplies(Long userId);
    List<Wall> findByUserIdAndSourceWallId(Long userId, Long sourceWallId);
    List<Wall> findByUserIdAndArticleId(Long userId, Long articleId);
    Optional<Wall> findById(Long id);
    List<Wall> findByUserFriends(Long userId);
    boolean remove(Long id);
}
