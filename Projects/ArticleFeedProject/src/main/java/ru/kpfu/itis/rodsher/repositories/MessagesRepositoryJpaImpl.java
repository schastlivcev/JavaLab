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
}
