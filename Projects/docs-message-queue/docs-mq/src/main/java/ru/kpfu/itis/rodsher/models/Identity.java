package ru.kpfu.itis.rodsher.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Identity {
    private String surname;
    private String name;
    private String middleName;
    private boolean isMan;
    private Date birth;
    private String address;
    private long phone;
    private String email;

    public String getCountry() {
        return address.split(",")[0].trim();
    }

    public String getCity() {
        return address.split(",")[1].trim();
    }
}