package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.security.config.details.UserDetailsImpl;
import ru.kpfu.itis.rodsher.services.AuthService;
import ru.kpfu.itis.rodsher.services.ContentFiller;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/signUp")
public class SignUpController {
    @Autowired
    private AuthService authService;

    @Autowired
    private ContentFiller contentFiller;

    @GetMapping
    public String getForm(@AuthenticationPrincipal UserDetailsImpl userDetails, ModelMap map) {
        if(userDetails != null) {
            return "redirect:/user";
        }
        map.put("selected_country", contentFiller.EMPTY_COUNTRY_NAME);
        map.put("countries", contentFiller.getCountries());
        return "auth/sign_up";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping
    public String signUp(@RequestParam Map<String, String> params, ModelMap map) {
        Dto dto = authService.signUpFromMap(params);
        if(dto.getStatus().equals(Status.USER_REG_SUCCESS)) {
            return "auth/sign_up_success";
        }
        else {
            map.put("email", params.get("email"));
            map.put("name", params.get("name"));
            map.put("surname", params.get("surname"));
            map.put("sex", params.get("sex"));
            map.put("birthday", map.get("birthday"));
            map.put("selected_country", params.get("country"));
            map.put("countries", contentFiller.getCountries());

            map.put("errors", (List<String>) dto.get("errors"));
            return "auth/sign_up";
        }
    }
}