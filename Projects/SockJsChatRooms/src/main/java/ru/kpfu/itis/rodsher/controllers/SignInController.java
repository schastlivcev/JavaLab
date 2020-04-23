package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.rodsher.services.UsersService;


@Controller
@RequestMapping("/signIn")
public class SignInController {
    @Autowired
    private UsersService usersService;

    @GetMapping
    public String getSignInForm() {
        return "auth/sign_in";
    }
}
