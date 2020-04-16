package ru.kpfu.itis.rodsher.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.rodsher.models.Channel;
import ru.kpfu.itis.rodsher.models.ChannelsToUsers;
import ru.kpfu.itis.rodsher.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class ChannelsRepositoryJpaImpl implements ChannelsRepository {
    private static final String FIND_BY_USER_ID = "SELECT c FROM ChannelsToUsers c WHERE c.user.id = :id";
    private static final String FIND_BY_CHANNEL_ID = "SELECT c FROM ChannelsToUsers c WHERE c.channel.id = :id";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long save(Channel channel) {
        entityManager.persist(channel);
        return channel.getId();
    }

    @Override
    @Transactional
    public boolean addUserToChannel(Channel channel, User user) {
        entityManager.merge(ChannelsToUsers.builder()
                .channel(channel)
                .user(user)
                .build());
        return true;
    }

    @Override
    @Transactional
    public List<Channel> findChannelsByUserId(Long userId) {
        List<ChannelsToUsers> channelsToUsers = entityManager.createQuery(FIND_BY_USER_ID, ChannelsToUsers.class).setParameter("id", userId).getResultList();
        List<Channel> channels = new ArrayList<>();
        for(ChannelsToUsers channel : channelsToUsers) {
            channels.add(channel.getChannel());
        }
        return channels;
    }

    @Override
    @Transactional
    public Optional<Channel> find(Long id) {
        return Optional.ofNullable(entityManager.find(Channel.class, id));
    }

    @Override
    public List<User> findUsersForChannelId(Long channelId) {
        List<ChannelsToUsers> channelsToUsers = entityManager.createQuery(FIND_BY_CHANNEL_ID, ChannelsToUsers.class).setParameter("id", channelId).getResultList();
        List<User> users = new ArrayList<>();
        for(ChannelsToUsers channel : channelsToUsers) {
            users.add(channel.getUser());
        }
        return users;
    }
}