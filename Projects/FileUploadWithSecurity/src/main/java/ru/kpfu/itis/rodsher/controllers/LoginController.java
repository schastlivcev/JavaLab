package ru.kpfu.itis.rodsher.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
@RequestMapping("/login")
public class LoginController {

    @PreAuthorize("isAnonymous()")
    @GetMapping
    public String getLoginForm(@RequestParam(value = "error", required = false) String error, ModelMap map) {
        if(error != null) {
            map.put("errors", Collections.singleton("No such user!"));
        }
        return "login";
    }
}