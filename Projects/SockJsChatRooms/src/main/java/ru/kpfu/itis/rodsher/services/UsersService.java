package ru.kpfu.itis.rodsher.services;

import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.dto.Dto;

import java.util.Map;

@Service
public interface UsersService {
    Dto signUpFromMap(Map<String, String> regForm);
    Dto signIn(String email, String password);
}
