package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/confirm")
public class EmailController {
    @Autowired
    private UserService userService;

    @PreAuthorize("permitAll()")
    @GetMapping
    public String confirm(HttpServletRequest req, HttpServletResponse resp, ModelMap map) {
        Dto dto = userService.validateToken(req, resp);
        String status = null;
        if(dto.getStatus().equals(Status.USER_VERIF_SUCCESS)) {
            User user = (User) dto.getFromPayload("user");
            req.setAttribute("name", user.getName());
            status = "verified";
        }
        if(dto.getStatus().equals(Status.USER_VERIF_EXPIRED)) {
            User user = (User) dto.getFromPayload("user");
            req.setAttribute("name", user.getName());
            status = "expired";
        }
        map.put("status", status);
        return "confirm";
    }
}