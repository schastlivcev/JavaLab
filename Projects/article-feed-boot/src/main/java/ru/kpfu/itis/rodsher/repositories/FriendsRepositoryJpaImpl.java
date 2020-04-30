package ru.kpfu.itis.rodsher.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.rodsher.models.Friends;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class FriendsRepositoryJpaImpl implements FriendsRepository {
    private static final String FIND_BY_IDS = "SELECT f FROM Friends f WHERE f.userSender.id = :id1 AND f.userRecipient.id = :id2 OR f.userSender.id = :id2 AND f.userRecipient.id = :id1";
    private static final String FIND_FOR_ID = "SELECT f FROM Friends f WHERE f.userSender.id = :id OR f.userRecipient.id = :id";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveStatus(Friends friends) {
        entityManager.merge(friends);
    }

    @Override
    @Transactional
    public boolean remove(Friends friends) {
        Optional<Friends> friendsOptional = find(friends.getUserSender().getId(), friends.getUserRecipient().getId());
        if(friendsOptional.isPresent()) {
            entityManager.remove(friendsOptional.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Friends> find(Long id1, Long id2) {
        try {
            return Optional.ofNullable(entityManager.createQuery(FIND_BY_IDS, Friends.class)
                    .setParameter("id1", id1)
                    .setParameter("id2", id2).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Friends> findFriendsForId(Long userId) {
        return entityManager.createQuery(FIND_FOR_ID, Friends.class).setParameter("id", userId).getResultList();
    }
}