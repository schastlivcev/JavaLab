package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.Channel;
import ru.kpfu.itis.rodsher.models.User;

import java.util.List;
import java.util.Optional;

public interface ChannelsRepository {
    Long save(Channel channel);
    boolean addUserToChannel(Channel channel, User user);
    List<Channel> findChannelsByUserId(Long userId);
    Optional<Channel> find(Long id);
    List<User> findUsersForChannelId(Long channelId);
    boolean checkUserBelongingToChannel(Long userId, Long channelId);
}
