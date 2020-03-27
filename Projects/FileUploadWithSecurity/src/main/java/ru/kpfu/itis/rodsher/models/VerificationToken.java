package ru.kpfu.itis.rodsher.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken {
//    private static final int EXPIRATION_MINUTES = 60 * 24;

    private int id;
    private int userId;
    private String token;
    private Date expiryDate;

//    private Date calculateExpiryDate(int expirationMinutes) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
//        calendar.add(Calendar.MINUTE, expirationMinutes);
//        return new Date(calendar.getTime().getTime());
//    }
}
