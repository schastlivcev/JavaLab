package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.services.UsersService;

import java.util.Map;

@Controller
@RequestMapping("/signUp")
public class SignUpController {
    @Autowired
    private UsersService usersService;

    @GetMapping
    public String getSignUpForm() {
        return "auth/sign_up";
    }

    @PostMapping
    public String signUp(@RequestParam Map<String, String> regForm, ModelMap map) {
        Dto dto = usersService.signUpFromMap(regForm);
        if(dto.getStatus().equals(Status.USER_REG_SUCCESS)) {
            return "redirect:/signUp/success";
        } else {
            map.put("errors", dto.get("errors"));

            map.put("email", regForm.get("email"));
            map.put("name", regForm.get("name"));
            map.put("sex", regForm.get("sex"));

            return "auth/sign_up";
        }
    }

    @GetMapping("/success")
    public String signUpSuccess(ModelMap map) {
        map.put("success", true);
        return "auth/sign_up";
    }
}