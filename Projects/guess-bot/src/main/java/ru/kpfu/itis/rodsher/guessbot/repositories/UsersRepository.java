package ru.kpfu.itis.rodsher.guessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;
import ru.kpfu.itis.rodsher.guessbot.models.User;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByClientTypeAndClientId(ClientType clientType, String clientId);
    Optional<User> findByName(String name);
}