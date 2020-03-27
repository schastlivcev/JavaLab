package ru.kpfu.itis.rodsher.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController {

    @PreAuthorize("permitAll()")
    @GetMapping
    public String getRootPage(Authentication authentication) {
        if(authentication != null) {
            return "redirect:/files";
        }
        return "redirect:/login";
    }
}