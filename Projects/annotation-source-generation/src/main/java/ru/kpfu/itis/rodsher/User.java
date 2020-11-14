package ru.kpfu.itis.rodsher;

import ru.kpfu.itis.rodsher.annotations.HtmlForm;
import ru.kpfu.itis.rodsher.annotations.HtmlInput;

@HtmlForm(method = "post", action = "/users", name = "User")
public class User {
    @HtmlInput(type = "text", name = "first_name", placeholder = "Имя")
    private String firstName;
    @HtmlInput(type = "text", name = "last_name", placeholder = "Фамилия")
    private String lastName;
    @HtmlInput(type = "email", name = "email", placeholder = "Email")
    private String email;
    @HtmlInput(type = "password", name = "password", placeholder = "Пароль")
    private String password;
}