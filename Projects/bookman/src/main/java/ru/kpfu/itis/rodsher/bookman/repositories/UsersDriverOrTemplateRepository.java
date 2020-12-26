package ru.kpfu.itis.rodsher.bookman.repositories;

import ru.kpfu.itis.rodsher.bookman.models.User;

import java.util.List;
import java.util.Optional;

public interface UsersDriverOrTemplateRepository {
    Optional<User> find(String id);
    List<User> findAll();
    void save(User user);
    void delete(String id);
    void update(User user);
}
