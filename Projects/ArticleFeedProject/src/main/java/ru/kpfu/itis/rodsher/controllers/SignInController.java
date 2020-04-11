package ru.kpfu.itis.rodsher.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;

@Controller
@RequestMapping("/signIn")
public class SignInController {

    @GetMapping
    public String getForm(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "error", required = false) String error, ModelMap map) {
        if(userDetails != null) {
            return "redirect:/user";
        }
        if(error != null) {
            map.put("error", "USER_NOT_FOUND");
        }
        return "auth/sign_in";
    }
}
