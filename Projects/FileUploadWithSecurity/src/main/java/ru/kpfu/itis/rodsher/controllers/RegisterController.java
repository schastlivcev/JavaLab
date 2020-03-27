package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserService userService;

    @PreAuthorize("isAnonymous()")
    @GetMapping
    public String getRegisterForm() {
        return "register";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping
    public String register(HttpServletRequest req, HttpServletResponse resp, ModelMap map) {
        Dto dto = userService.register(req, resp);
        if(dto.getStatus().equals(Status.USER_REG_SUCCESS)) {
            map.put("success", true);
        }
        else {
            map.put("errors", (List<String>) dto.getFromPayload("errors"));
        }
        return "register";
    }
}
