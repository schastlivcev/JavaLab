package ru.kpfu.itis.rodsher.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class RootController {

    @GetMapping
    public String getWelcomePage(HttpSession session) {
        if(session.getAttribute("user") != null) {
            return "redirect:/rooms";
        }
        return "auth/welcome";
    }
}
