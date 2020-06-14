package ru.kpfu.itis.rodsher.guessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.guessbot.models.Channel;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;

import java.util.Optional;

@Repository
public interface ChannelsRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByClientTypeAndClientId(ClientType clientType, String clientId);
}
