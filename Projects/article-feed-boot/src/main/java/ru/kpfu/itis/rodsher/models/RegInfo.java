package ru.kpfu.itis.rodsher.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegInfo {
    private String email;
    private String password;
    private String passwordRepeat;
    private String name;
    private String surname;
    private boolean sex;
    private Country country;
    private Date birthday;
    private boolean agreement;
}
