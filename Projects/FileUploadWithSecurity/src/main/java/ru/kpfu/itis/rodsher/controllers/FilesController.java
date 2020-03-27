package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.FilesService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class FilesController {

    @Autowired
    private FilesService filesService;

    // Security Version
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/files")
    public String getFile(Authentication auth, ModelMap map) {
        User user = ((UserDetailsImpl) auth.getPrincipal()).getUser();
        map.put("name", user.getName());
        return "file_upload";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/files")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile, Authentication auth, ModelMap map) {
        User user = ((UserDetailsImpl) auth.getPrincipal()).getUser();

        Dto dto = filesService.downloadFileToStorage(multipartFile, "http://localhost:8080/files/", user);
        if(dto.getStatus().equals(Status.FILE_DOWNLOAD_SUCCESS)) {
            map.put("status", "success");
        }
        return "upload_status";
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value ="/files/{file-name:.+}")
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("file-name") String fileName, HttpServletResponse resp) throws IOException {
        Dto dto = filesService.uploadFileToClient(fileName);
        if(dto.getStatus().equals(Status.FILE_UPLOAD_ERROR)) {
            resp.sendError(404);
            return null;
        }
        return (FileSystemResource) dto.getFromPayload("file");
    }
}
