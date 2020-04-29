package ru.kpfu.itis.rodsher.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.rodsher.models.Wall;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class WallsRepositoryJpaImpl implements WallsRepository {
    private static final String FIND_BY_USER_ID_WITH_REPLIES = "SELECT w FROM Wall w WHERE w.user.id = :id AND w.bookmark = false ORDER BY w.createdAt DESC";
    private static final String FIND_BY_USER_ID_REPLIES = "SELECT w FROM Wall w WHERE w.user.id = :id AND w.reply = true ORDER BY w.createdAt DESC";
    private static final String FIND_BY_USER_ID_BOOKMARKS =  "SELECT w FROM Wall w WHERE w.user.id = :id AND w.bookmark = true ORDER BY w.createdAt DESC";
    private static final String FIND_BY_USER_ID_AND_SOURCE_ID =  "SELECT w FROM Wall w WHERE w.user.id = :id AND w.source.id = :sid ORDER BY w.createdAt DESC";
    private static final String FIND_BY_USER_ID_AND_ARTICLE_ID =  "SELECT w FROM Wall w WHERE w.user.id = :id AND w.article.id = :aid ORDER BY w.createdAt DESC";
    private static final String FIND_BY_USER_FRIENDS = "SELECT w FROM Wall w LEFT OUTER JOIN Friends f ON w.user.id = f.userSender.id OR w.user.id = f.userRecipient.id WHERE w.user.id <> :id AND w.bookmark = false AND f.status = ru.kpfu.itis.rodsher.models.FriendsStatus.ACCEPTED AND (f.userSender.id = :id OR f.userRecipient.id = :id) ORDER BY w.createdAt DESC";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long save(Wall wall) {
        entityManager.persist(wall);
        return wall.getId();
    }

    @Override
    @Transactional
    public List<Wall> findByUserIdWithReplies(Long userId) {
        return entityManager.createQuery(FIND_BY_USER_ID_WITH_REPLIES, Wall.class)
                .setParameter("id", userId).getResultList();
    }

    @Override
    @Transactional
    public List<Wall> findByUserIdBookmarks(Long userId) {
        return entityManager.createQuery(FIND_BY_USER_ID_BOOKMARKS, Wall.class)
                .setParameter("id", userId).getResultList();
    }

    @Override
    @Transactional
    public List<Wall> findByUserIdReplies(Long userId) {
        return entityManager.createQuery(FIND_BY_USER_ID_REPLIES, Wall.class)
                .setParameter("id", userId).getResultList();
    }

    @Override
    @Transactional
    public List<Wall> findByUserIdAndSourceWallId(Long userId, Long sourceWallId) {
        return entityManager.createQuery(FIND_BY_USER_ID_AND_SOURCE_ID, Wall.class).setParameter("id", userId)
                .setParameter("sid", sourceWallId).getResultList();
    }

    @Override
    @Transactional
    public List<Wall> findByUserIdAndArticleId(Long userId, Long articleId) {
        return entityManager.createQuery(FIND_BY_USER_ID_AND_ARTICLE_ID, Wall.class).setParameter("id", userId)
                .setParameter("aid", articleId).getResultList();
    }

    @Override
    @Transactional
    public List<Wall> findByUserFriends(Long userId) {
        return entityManager.createQuery(FIND_BY_USER_FRIENDS, Wall.class).setParameter("id", userId).getResultList();
    }

    @Override
    @Transactional
    public Optional<Wall> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Wall.class, id));
    }


    @Override
    @Transactional
    public boolean remove(Long id) {
        Optional<Wall> wallOptional = findById(id);
        if(wallOptional.isPresent()) {
            entityManager.remove(wallOptional.get());
            return true;
        } else {
            return false;
        }
    }
}
