package ru.kpfu.itis.rodsher.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalData {
    private String name;
    private String surname;
    private String middleName;
    private int age;
    private Passport passport;
}