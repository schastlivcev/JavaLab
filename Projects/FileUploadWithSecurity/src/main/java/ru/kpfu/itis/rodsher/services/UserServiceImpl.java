package ru.kpfu.itis.rodsher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.dto.WebDto;
import ru.kpfu.itis.rodsher.models.Role;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.models.VerificationToken;
import ru.kpfu.itis.rodsher.registration.OnRegistrationCompleteEvent;
import ru.kpfu.itis.rodsher.repositories.UsersRepository;
import ru.kpfu.itis.rodsher.repositories.VerificationTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Dto login(HttpServletRequest req, HttpServletResponse resp) {
        List<String> errors = new ArrayList<>();
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(passwordEncoder.matches(password, user.getPassword())) {
                if(user.isVerified()) {
                    return new WebDto(Status.USER_LOGIN_SUCCESS, "user", user);
                }
                errors.add("You have not verified your account yet!");
                return new WebDto(Status.USER_LOGIN_NOT_VERIFIED, "errors", errors);
            }
            errors.add("Wrong password!");
            return new WebDto(Status.USER_LOGIN_WRONG_PASSWORD, "errors", errors);
        }
        errors.add("No such user!");
        return new WebDto(Status.USER_LOGIN_NO_SUCH_USER, "errors", errors);
    }

    @Override
    public Dto register(HttpServletRequest req, HttpServletResponse resp) {
        String email = req.getParameter("email");
        req.setAttribute("email", email);
        String password = req.getParameter("password");
        String repeatPas = req.getParameter("reppassword");
        String name = req.getParameter("name");
        req.setAttribute("name", name);
        String sex = req.getParameter("sex");
        req.setAttribute("sex", sex);
        List<String> errors = new ArrayList<>();
        if(email.trim().equals("")) {
            errors.add("Empty Email field!");
        }
        else if(!email.matches(".+@.+\\..+")) {
            errors.add("Wrong Email format!");
        }
        if(password.trim().equals("")) {
            errors.add("Empty Password field!");
        }
        else if(!password.equals(repeatPas)) {
            errors.add("Passwords do not match!");
        }
        if(name.trim().equals("")) {
            errors.add("Empty Name field!");
        }
        if(!sex.equals("female") && !sex.equals("male")) {
            errors.add("Incorrect Sex!");
        }

        if(errors.size() == 0) {
            if(usersRepository.checkAvailability(email, name)) {
                boolean isMan = sex.equals("male");
                String passHashed = passwordEncoder.encode(password);
                Integer key = usersRepository.save(new User(null, email, passHashed, name, isMan, false, Role.USER));
                if(key != null) {
                    User user = new User(key, email, password, name, isMan, false, Role.USER);
                    try {
                        publisher.publishEvent(new OnRegistrationCompleteEvent(req.getContextPath(), user));
                        return new WebDto(Status.USER_REG_SUCCESS, "user", user);
                    }
                    catch (Exception e) {
                        usersRepository.removeById(key);
                        errors.add("Email sending error. Please try again.");
                    }
                }
                else {
                    errors.add("Database saving error. Please try again.");
                }
            }
            else {
                errors.add("User with same email and name already exists!");
            }
        }
        return new WebDto(Status.USER_REG_ERROR, "errors", errors);
    }

    @Override
    public Dto validateToken(HttpServletRequest req, HttpServletResponse resp) {
        Optional<VerificationToken> tokenOptional = getVerificationToken((String) req.getParameter("token"));
        if(!tokenOptional.isPresent()) {
            return new WebDto(Status.USER_VERIF_EMPTY);
        }
        VerificationToken verificationToken = tokenOptional.get();

        int userId = verificationToken.getUserId();
        Optional<User> userOptional = usersRepository.findById(userId);
        if(!userOptional.isPresent()) {
            return new WebDto(Status.USER_VERIF_EMPTY);
        }
        User user = userOptional.get();

        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpiryDate().getTime()) - calendar.getTime().getTime() <= 0) {
            return new WebDto(Status.USER_VERIF_EXPIRED, "user", user);
        }
        usersRepository.setVerified(userId);
        verificationTokenRepository.removeById(verificationToken.getId());

        return new WebDto(Status.USER_VERIF_SUCCESS, "user", user);
    }

    @Override
    public void createVerificationToken(int userId, String token, Date expiryDate) {
        verificationTokenRepository.save(userId, token, expiryDate);
    }

    @Override
    public Optional<VerificationToken> getVerificationToken(String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken);
    }

}
