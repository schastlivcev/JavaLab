package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.Role;
import ru.kpfu.itis.rodsher.models.User;

import java.util.Optional;

public interface UsersRepository {
    Integer save(User user);
    boolean checkAvailability(String email, String name);
    boolean checkVerification(int id);
    boolean setVerified(int id);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
    boolean removeById(int id);
}
