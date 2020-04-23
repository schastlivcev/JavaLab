package ru.kpfu.itis.rodsher.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.dto.WebDto;
import ru.kpfu.itis.rodsher.models.Role;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.repositories.UsersRepository;

import java.util.*;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Dto signIn(String email, String password) {
        List<String> errors = new ArrayList<>();
        if(email == null || email.trim().equals("")) {
            errors.add("Empty email");
        } else if(!email.matches(".+@.+\\..+") || !email.trim().equals(email)) {
            errors.add("Unacceptable email");
        }

        if(password == null || password.trim().equals("")) {
            errors.add("Empty password");
        } else if(!password.trim().equals(password)) {
            errors.add("Unacceptable password");
        } else if(password.length() < 8) {
            errors.add("Password is too short");
        }

        System.out.println(email + ", " + password + ", errors:" + errors);
        if(errors.isEmpty()) {
            Optional<User> userOptional = usersRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                System.out.println("user is present");
                User user = userOptional.get();
                if (passwordEncoder.matches(password, user.getPassword())) {
                    System.out.println("pass matches");
                    String token = Jwts.builder()
                            .setSubject(user.getId().toString())
                            .claim("name", user.getName())
                            .claim("role", user.getRole().name())
                            .signWith(SignatureAlgorithm.HS256, secret)
                            .compact();
                    Map<String, Object> map = new HashMap<>();
                    map.put("user", user);
                    map.put("token", token);
                    return new WebDto(Status.USER_SIGN_IN_SUCCESS, map);
                }
            }
        }
        return new WebDto(Status.USER_SIGN_IN_ERROR);
    }

    @Override
    public Dto signUpFromMap(Map<String, String> regForm) {
        List<String> errors = new ArrayList<>();
        verifySignUpForm(regForm, errors);
        if(errors.isEmpty()) {
            usersRepository.save(User.builder()
                    .email(regForm.get("email"))
                    .password(passwordEncoder.encode(regForm.get("password")))
                    .name(regForm.get("name"))
                    .isMan(regForm.get("sex").equals("true"))
                    .role(Role.USER)
                    .build());
            return new WebDto(Status.USER_REG_SUCCESS);
        } else {
            return new WebDto(Status.USER_REG_ERROR, "errors", errors);
        }
    }

    private void verifySignUpForm(Map<String, String> regForm, List<String> errors) {
        String email = regForm.get("email");
        if(email == null || email.trim().equals("")) {
            errors.add("Empty email");
        } else if(!email.matches(".+@.+\\..+") || !email.trim().equals(email)) {
            errors.add("Unacceptable email");
        } else if(usersRepository.findByEmail(email).isPresent()) {
            errors.add("Email is already used");
        }

        String password = regForm.get("password");
        if(password == null || password.trim().equals("")) {
            errors.add("Empty password");
        } else if(!password.trim().equals(password)) {
            errors.add("Unacceptable password");
        } else if(password.length() < 8) {
            errors.add("Password is too short");
        }
        String passwordRepeat = regForm.get("password_repeat");
        if(passwordRepeat != null && password != null && !password.equals(passwordRepeat)) {
            errors.add("Passwords do not match");
        }
        String name = regForm.get("name");
        if(name == null || name.trim().equals("")) {
            errors.add("Empty name");
        } else if(!name.trim().equals(name) || name.length() < 1 || name.length() > 30) {
            errors.add("Unacceptable name");
        }

        String sex = regForm.get("sex");
        if(sex == null || sex.trim().equals("")) {
            errors.add("Please choose sex");
        } else if(!(sex.equals("true") || sex.equals("false"))) {
            errors.add("Unacceptable sex");
        }

    }
}
