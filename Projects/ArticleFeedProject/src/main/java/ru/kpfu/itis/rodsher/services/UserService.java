package ru.kpfu.itis.rodsher.services;

import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.User;

import java.util.Optional;

public interface UserService {
    Dto loadUser(long id);
}
