package ru.kpfu.itis.rodsher.services;

import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.VerificationToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.Optional;

public interface UserService {
    Dto register(HttpServletRequest req, HttpServletResponse resp);
    Dto login(HttpServletRequest req, HttpServletResponse resp);
    Dto validateToken(HttpServletRequest req, HttpServletResponse resp);
    void createVerificationToken(int userId, String token, Date expiryDate);
    Optional<VerificationToken> getVerificationToken(String verificationToken);
}
