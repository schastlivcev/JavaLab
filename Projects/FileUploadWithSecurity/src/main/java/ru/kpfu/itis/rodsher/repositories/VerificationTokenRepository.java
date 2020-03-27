package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.VerificationToken;

import java.sql.Date;
import java.util.Optional;

public interface VerificationTokenRepository {
    Integer save(int userId, String token, Date expiryDate);
    Optional<VerificationToken> findByToken(String token);
    boolean removeById(int id);
}
