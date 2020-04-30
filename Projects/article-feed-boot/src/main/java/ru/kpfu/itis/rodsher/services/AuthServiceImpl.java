package ru.kpfu.itis.rodsher.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.dto.WebDto;
import ru.kpfu.itis.rodsher.models.Country;
import ru.kpfu.itis.rodsher.models.RegInfo;
import ru.kpfu.itis.rodsher.models.Role;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.repositories.CountriesRepository;
import ru.kpfu.itis.rodsher.repositories.UsersRepository;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CountriesRepository countriesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Dto signUpFromClass(RegInfo regInfo) {
        return signUpFromClassWithErrors(regInfo, new ArrayList<>());
    }

    @Override
    public Dto signUpFromMap(Map<String, String> formAttributes) {
        List<String> errors = new ArrayList<>();
        try {
            String c = formAttributes.get("country");
            Optional<Country> countryOptional = countriesRepository.findByNameRu(c);
            if(!countryOptional.isPresent()) {
                errors.add("DATA_UNACCEPTABLE");
                return new WebDto(Status.USER_SIGN_UP_ERROR, "errors", errors);
            }
            Country country = countryOptional.get();
            Map<String, Object> objectMap = new HashMap<>(formAttributes);
            objectMap.put("country", country);
            objectMap.remove("_csrf");
            RegInfo regInfo = objectMapper.convertValue(objectMap, RegInfo.class);

            return signUpFromClassWithErrors(regInfo, errors);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            errors.add("DATA_UNACCEPTABLE");
            return new WebDto(Status.USER_SIGN_UP_ERROR, "errors", errors);
        }
    }

    private Dto signUpFromClassWithErrors(RegInfo regInfo, List<String> errors) {
        verifySignUpForm(regInfo, errors);
        if(errors.isEmpty()) {
            usersRepository.save(User.builder()
                    .email(regInfo.getEmail())
                    .password(passwordEncoder.encode(regInfo.getPassword()))
                    .name(regInfo.getName())
                    .surname(regInfo.getSurname())
                    .isMan(regInfo.isSex())
                    .birthday(regInfo.getBirthday())
                    .country(regInfo.getCountry())
                    .role(Role.USER)
                    .verified(true)
                    .build());
            return new WebDto(Status.USER_SIGN_UP_SUCCESS);
        } else {
            return new WebDto(Status.USER_SIGN_UP_ERROR, "errors", errors);
        }
    }

    private void verifySignUpForm(RegInfo regInfo, List<String> errors) {
        if(regInfo.getEmail() == null || regInfo.getEmail().trim().equals("")) {
            errors.add("EMAIL_EMPTY");
        } else if(!regInfo.getEmail().matches(".+@.+\\..+") || !regInfo.getEmail().trim().equals(regInfo.getEmail())) {
            errors.add("EMAIL_UNACCEPTABLE");
        } else if(usersRepository.findByEmail(regInfo.getEmail()).isPresent()) {
            errors.add("EMAIL_UNAVAILABLE");
        }

        if(regInfo.getPassword() == null || regInfo.getPassword().trim().equals("")) {
            errors.add("PASSWORD_EMPTY");
        } else if(!regInfo.getPassword().trim().equals(regInfo.getPassword())) {
            errors.add("PASSWORD_UNACCEPTABLE");
        } else if(regInfo.getPassword().length() < 8) {
            errors.add("PASSWORD_SHORT");
        }

        if(regInfo.getPasswordRepeat() != null && !regInfo.getPassword().equals(regInfo.getPasswordRepeat())) {
            errors.add("PASSWORD_MISMATCH");
        }

        if(regInfo.getName() == null || regInfo.getName().trim().equals("")) {
            errors.add("NAME_EMPTY");
        } else if(!regInfo.getName().trim().equals(regInfo.getName()) || regInfo.getName().length() < 1 || regInfo.getName().length() > 30) {
            errors.add("NAME_UNACCEPTABLE");
        }

        if(regInfo.getSurname() == null || regInfo.getSurname().trim().equals("")) {
            errors.add("SURNAME_EMPTY");
        } else if(!regInfo.getSurname().trim().equals(regInfo.getSurname()) || regInfo.getSurname().length() < 1 || regInfo.getSurname().length() > 60) {
            errors.add("SURNAME_UNACCEPTABLE");
        }

        if(regInfo.getBirthday() == null) {
            errors.add("BIRTHDAY_EMPTY");
        } else if(regInfo.getBirthday().getTime() < -1577934000000L || regInfo.getBirthday().getTime() > System.currentTimeMillis() - 31556952000L) {
            errors.add("BIRTHDAY_UNACCEPTABLE");
        }

        if(!regInfo.isAgreement()) {
            errors.add("AGREEMENT_UNCHECKED");
        }
    }
}
