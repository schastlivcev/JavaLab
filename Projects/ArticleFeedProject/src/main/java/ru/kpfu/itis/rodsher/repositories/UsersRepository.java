package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.User;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    Long save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
    List<User> findByNameAndSurname(String name, String surname);
    boolean updateInfo(User user);
}
