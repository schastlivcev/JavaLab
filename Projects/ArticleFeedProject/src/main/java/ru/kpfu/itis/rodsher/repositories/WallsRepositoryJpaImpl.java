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
    private static final String FIND_BY_USER_ID = "SELECT w FROM Wall w WHERE w.user.id = :id ORDER BY w.createdAt DESC";

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
    public List<Wall> findByUserId(Long userId) {
        return entityManager.createQuery(FIND_BY_USER_ID, Wall.class).setParameter("id", userId).getResultList();
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
