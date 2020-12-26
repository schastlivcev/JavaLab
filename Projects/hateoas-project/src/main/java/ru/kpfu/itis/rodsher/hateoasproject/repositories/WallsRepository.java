package ru.kpfu.itis.rodsher.hateoasproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.hateoasproject.models.Wall;

@Repository
public interface WallsRepository extends JpaRepository<Wall, Long> {
}
