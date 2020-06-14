package ru.kpfu.itis.rodsher.guessbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.rodsher.guessbot.models.ChannelsToUsers;

import java.util.List;

public interface ChannelsToUsersRepository extends JpaRepository<ChannelsToUsers, Long> {
    List<ChannelsToUsers> findByChannel_Id(Long channelId);
    List<ChannelsToUsers> findByUser_Id(Long userId);
    @Transactional
    void deleteByUser_Id(Long userId);
}
