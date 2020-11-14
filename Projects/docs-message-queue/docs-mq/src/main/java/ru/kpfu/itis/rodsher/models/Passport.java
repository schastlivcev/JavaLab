package ru.kpfu.itis.rodsher.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Passport {
    private String surname;
    private String name;
    private String middleName;
    private boolean isMan;
    private Date birth;
    private String city;
    private String address;
    private long series;
    private String issuedBy;
    private long issuedByCode;
    private Date issueDate;
}
