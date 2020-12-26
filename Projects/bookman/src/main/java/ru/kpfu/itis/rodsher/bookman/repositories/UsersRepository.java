package ru.kpfu.itis.rodsher.bookman.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.bookman.models.User;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {
}
