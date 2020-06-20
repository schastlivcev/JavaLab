package ru.kpfu.itis.rodsher.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegInfo {
    @Email(message = "{sign.email.error.unacceptable}")
    private String email;
    @NotBlank(message = "{sign.password.error.empty}")
    @Size(min = 8, message = "{sign.password.error.short}")
    private String password;
    private String passwordRepeat;
    @NotBlank(message = "{sign.name.error.empty}")
    @Size(max = 30, message = "{sign.name.error.unacceptable}")
    private String name;
    @NotBlank(message = "{sign.surname.error.empty}")
    @Size(max = 40, message = "{sign.surname.error.unacceptable}")
    private String surname;
    @NotNull
    private boolean sex;
    @NotNull
    private Country country;
    @NotNull
    private Date birthday;
    @NotNull
    private boolean agreement;
}
