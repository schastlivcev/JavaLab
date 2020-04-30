package ru.kpfu.itis.rodsher.services;

import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.RegInfo;

import java.util.Map;

@Service
public interface AuthService {
    Dto signUpFromMap(Map<String, String> formAttributes);
    Dto signUpFromClass(RegInfo regInfo);
}
