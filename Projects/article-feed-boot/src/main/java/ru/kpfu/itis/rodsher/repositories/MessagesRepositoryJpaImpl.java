package ru.kpfu.itis.rodsher.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.rodsher.models.Message;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class MessagesRepositoryJpaImpl implements MessagesRepository {
    private static final String FIND_BY_CHANNEL_ID = "SELECT m FROM Message m WHERE m.channel.id = :id";
    private static final String FIND_LAST_FOR_USER_ID_CHANNELS = "SELECT m.id, m.content, m.created_at, m.author_id, m.channel_id FROM (SELECT m.channel_id, MAX(m.created_at) AS max FROM messages m GROUP BY  m.channel_id) as x INNER JOIN messages m ON m.channel_id = x.channel_id AND m.created_at = max INNER JOIN channels_to_users ctu on m.channel_id = ctu.channel_id WHERE ctu.user_id = ?1 ORDER BY m.created_at DESC";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long save(Message message) {
        entityManager.persist(message);
        return message.getId();
    }

    @Override
    @Transactional
    public List<Message> findMessagesByChannelId(Long channelId) {
        return entityManager.createQuery(FIND_BY_CHANNEL_ID, Message.class).setParameter("id", channelId).getResultList();
    }

    @Override
    @Transactional
    public Optional<Message> find(Long id) {
        return Optional.ofNullable(entityManager.find(Message.class, id));
    }

    @Override
    @Transactional
    public List<Message> findLastMessagesForChannelsByUserId(Long userId) {
        return entityManager.createNativeQuery(FIND_LAST_FOR_USER_ID_CHANNELS, Message.class).setParameter(1, userId).getResultList();
    }
}
