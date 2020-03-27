package ru.kpfu.itis.rodsher.registration;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.services.FreeMarkerTemplateEmailSender;
import ru.kpfu.itis.rodsher.services.UserService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private UserService userService;

    @Autowired
    private FreeMarkerTemplateEmailSender emailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        userService.createVerificationToken(user.getId(), token, new Date(calendar.getTime().getTime()));

        String recipientAddress = user.getEmail();
        String subject = "JavaLab Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/confirm?token=" + token;

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("url", "http://localhost:8080" + confirmationUrl);

        try {
            emailSender.sendEmail(recipientAddress, "JavaLab Solemn Signing Up confirmation",
                    model, "verify_letter");
        } catch (MessagingException | TemplateException | IOException e) {
            System.err.println("FreeMarker killed. Connection loo.ooooo.....ooo.ooooo..oss..........sss........t.......tt");
        }
    }
}
