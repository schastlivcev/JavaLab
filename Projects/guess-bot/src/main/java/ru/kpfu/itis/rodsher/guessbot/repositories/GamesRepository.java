package ru.kpfu.itis.rodsher.guessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.rodsher.guessbot.models.Game;

public interface GamesRepository extends JpaRepository<Game, Long> {
}
