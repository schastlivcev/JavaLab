package ru.kpfu.itis.rodsher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String getResults(@RequestParam(name = "q", required = false) String query, ModelMap map) {
        if(query != null) {
            String[] parts = query.split(" ");
            if(parts.length == 1) {
                map.put("users", (List<User>) userService.loadUsersByNameAndSurname(parts[0], "").get("users"));
            } else if(parts.length == 2) {
                map.put("users", (List<User>) userService.loadUsersByNameAndSurname(parts[0], parts[1]).get("users"));
            } else {
                map.put("error", "INVALID_QUERY");
            }
        }
        map.put("query", query);
        return "main/search";
    }
}