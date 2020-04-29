package ru.kpfu.itis.rodsher.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;

@Controller
@RequestMapping("/")
public class RootContoller {

    @PreAuthorize("permitAll()")
    @GetMapping
    public String getPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails != null) {
            return "redirect:/feed";
        }
        return "auth/welcome";
    }
}
