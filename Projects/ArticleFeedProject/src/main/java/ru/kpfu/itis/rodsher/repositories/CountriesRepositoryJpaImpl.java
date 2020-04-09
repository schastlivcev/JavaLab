package ru.kpfu.itis.rodsher.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.rodsher.models.Country;
import ru.kpfu.itis.rodsher.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class CountriesRepositoryJpaImpl implements CountriesRepository {
    private static final String FIND_ALL = "SELECT c FROM Country c";
    private static final String FIND_BY_NAME_RU = "SELECT c FROM Country c WHERE c.nameRu = :name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<Country> findById(int id) {
        return Optional.ofNullable(entityManager.find(Country.class, id));
    }

    @Override
    @Transactional
    public List<Country> findAll() {
        return (List<Country>) entityManager.createQuery(FIND_ALL).getResultList();
    }

    @Override
    public Optional<Country> findByNameRu(String nameRu) {
        try {
            return Optional.ofNullable(entityManager.createQuery(FIND_BY_NAME_RU, Country.class).setParameter("name", nameRu).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
