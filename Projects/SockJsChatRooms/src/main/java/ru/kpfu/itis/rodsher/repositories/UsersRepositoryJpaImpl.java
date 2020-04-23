package ru.kpfu.itis.rodsher.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.rodsher.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class UsersRepositoryJpaImpl implements UsersRepository {
    private static final String FIND_BY_EMAIL = "SELECT u FROM User u WHERE u.email = :email";
    private static final String FIND_BY_NAME_AND_SURNAME = "SELECT u FROM User u WHERE lower(u.name) LIKE :name AND lower(u.surname) LIKE :surname OR lower(u.surname) LIKE :name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long save(User user) {
        entityManager.persist(user);
        return user.getId();
    }

    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.ofNullable(entityManager.createQuery(FIND_BY_EMAIL, User.class).setParameter("email", email).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<User> findById(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> findByNameAndSurname(String name, String surname) {
        return entityManager.createQuery(FIND_BY_NAME_AND_SURNAME, User.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .setParameter("surname", "%" + surname.toLowerCase() + "%")
                .getResultList();
    }
}