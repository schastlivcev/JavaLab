package ru.kpfu.itis.rodsher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.RegInfo;
import ru.kpfu.itis.rodsher.repositories.UsersRepository;

import java.util.Map;

@Service
public interface AuthService {
    Dto signInRest(String email, String password);
    Dto signUpFromMap(Map<String, String> formAttributes);
    Dto signUpFromClass(RegInfo regInfo);
}
