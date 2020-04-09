package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.Wall;

import java.util.List;
import java.util.Optional;

public interface WallsRepository {
    Long save(Wall wall);
    List<Wall> findByUserId(Long userId);
    Optional<Wall> findById(Long id);
    boolean remove(Long id);
}
