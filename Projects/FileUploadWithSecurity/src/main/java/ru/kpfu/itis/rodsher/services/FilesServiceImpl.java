package ru.kpfu.itis.rodsher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.dto.WebDto;
import ru.kpfu.itis.rodsher.models.FileInfo;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.repositories.FilesRepository;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class FilesServiceImpl implements FilesService {
    @Autowired
    private FilesRepository filesRepository;

    @Autowired
    private Environment environment;

    @Override
    public Dto downloadFileToStorage(MultipartFile file, String contextPath, User user) {
        String storageFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        try {
            file.transferTo(new File(environment.getProperty("storage.path") + storageFileName));
            filesRepository.save(new FileInfo(null, storageFileName, file.getOriginalFilename(), file.getSize(),
                    file.getContentType(), contextPath + storageFileName, user.getId()));
            return new WebDto(Status.FILE_DOWNLOAD_SUCCESS, "fileUrl", contextPath + storageFileName);
        } catch (IOException e) {
            return new WebDto(Status.FILE_DOWNLOAD_SAVING_ERROR);
        }
    }

    @Override
    public Dto uploadFileToClient(String storageFileName) {
        Optional<FileInfo> infoOptional = filesRepository.findByStorageFileName(storageFileName);
        if(!infoOptional.isPresent()) {
            return new WebDto(Status.FILE_UPLOAD_ERROR); //, "noSuchFileHtml", new FileSystemResource(environment.getProperty("storage.path") + "no_such_file.html"));
        }
        FileInfo fileInfo = infoOptional.get();
        FileSystemResource file = new FileSystemResource(environment.getProperty("storage.path") + fileInfo.getStorageFileName());

        return new WebDto(Status.FILE_UPLOAD_SUCCESS, "file", file);
    }
}
