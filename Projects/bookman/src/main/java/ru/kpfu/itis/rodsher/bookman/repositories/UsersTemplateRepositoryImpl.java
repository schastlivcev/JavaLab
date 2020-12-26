package ru.kpfu.itis.rodsher.bookman.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.bookman.models.User;

import java.util.List;
import java.util.Optional;

//@Repository
public class UsersTemplateRepositoryImpl implements UsersDriverOrTemplateRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Optional<User> find(String id) {
        List<User> users = mongoTemplate.find(new Query(Criteria.where("_id").is(id)), User.class, "users");
        if(users.size() == 0)
            return Optional.empty();
        return Optional.of(users.get(0));
    }

    @Override
    public List<User> findAll() {
        return mongoTemplate.findAll(User.class, "users");
    }

    @Override
    public void save(User user) {
        mongoTemplate.save(user, "users");
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), User.class, "users");
    }

    @Override
    public void update(User user) {
        mongoTemplate.save(user);
    }
}
