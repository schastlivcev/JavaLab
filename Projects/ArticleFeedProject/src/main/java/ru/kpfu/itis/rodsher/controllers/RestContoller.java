package ru.kpfu.itis.rodsher.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.dto.WebDto;
import ru.kpfu.itis.rodsher.models.RegInfo;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.security.config.details.UserDetailsJwtImpl;
import ru.kpfu.itis.rodsher.services.AuthService;
import ru.kpfu.itis.rodsher.services.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RestContoller {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/rest/signUp")
    public Map signUp(@RequestBody RegInfo params, ModelMap map) {
        return authService.signUpFromClass(params).getMap();
    }

    @PostMapping("/rest/signIn")
    public Map signIn(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map params = mapper.readValue(body, Map.class);
            if(params.get("email") != null && params.get("password") != null) {
                return authService.signInRest((String) params.get("email"), (String) params.get("password")).getMap();
            }
            return new WebDto(Status.USER_SIGN_IN_ERROR).getMap();
        } catch (IOException e) {
            return new WebDto(Status.USER_SIGN_IN_ERROR).getMap();
        }
    }

    @GetMapping("/rest/user")
    public Map getUserInfo(@AuthenticationPrincipal UserDetailsJwtImpl userDetails) {
        Dto dto = userService.loadUser(userDetails.getId());
        if(dto.getStatus().equals(Status.USER_LOAD_SUCCESS)) {
            Map map = dto.getMap();
            User user = (User) dto.get("user");
            map.remove("user");
            map.put("id", user.getId());
            map.put("email", user.getEmail());
            map.put("name", user.getName());
            map.put("surmame", user.getSurname());
            map.put("sex", user.getIsMan() ? "male" : "female");
            map.put("birthday", user.getBirthday());
            return map;
        }
        return dto.getMap();
    }
}