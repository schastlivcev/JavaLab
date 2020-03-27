package ru.kpfu.itis.rodsher.services;

import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.User;

public interface FilesService {
    Dto downloadFileToStorage(MultipartFile file, String contextPath, User user);
    Dto uploadFileToClient(String storageFileName);
}
