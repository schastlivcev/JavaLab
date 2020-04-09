package ru.kpfu.itis.rodsher.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.rodsher.models.Article;
import ru.kpfu.itis.rodsher.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class ArticlesRepositoryJpaImpl implements ArticlesRepository {
    private static final String FIND_BY_USER_ID = "SELECT a FROM Article a WHERE a.user.id = :id ORDER BY a.createdAt DESC";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long save(Article article) {
        entityManager.persist(article);
        return article.getId();
    }

    @Override
    @Transactional
    public List<Article> findByUserId(Long userId) {
        return entityManager.createQuery(FIND_BY_USER_ID, Article.class).setParameter("id", userId).getResultList();
    }

    @Override
    @Transactional
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Article.class, id));
    }


    @Override
    @Transactional
    public boolean remove(Long id) {
        Optional<Article> articleOptional = findById(id);
        if(articleOptional.isPresent()) {
            entityManager.remove(articleOptional.get());
            return true;
        } else {
            return false;
        }
    }
}
