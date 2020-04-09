package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.User;

import java.util.Optional;

public interface UsersRepository {
    Long save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
}
