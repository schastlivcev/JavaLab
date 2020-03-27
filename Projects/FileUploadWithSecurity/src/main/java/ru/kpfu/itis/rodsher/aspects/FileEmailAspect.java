package ru.kpfu.itis.rodsher.aspects;

import freemarker.template.TemplateException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.models.User;
import ru.kpfu.itis.rodsher.services.FreeMarkerTemplateEmailSender;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class FileEmailAspect {
    @Autowired
    private FreeMarkerTemplateEmailSender emailSender;

    @AfterReturning(pointcut = "execution(* ru.kpfu.itis.rodsher.services.FilesService.downloadFileToStorage(..))", returning = "returnDto")
    public void afterFileDownloading(JoinPoint joinPoint, Object returnDto) {
        Object[] args = joinPoint.getArgs();
        String url = (String) ((Dto) returnDto).getFromPayload("fileUrl");
        for(Object arg : args) {
            if(arg instanceof User) {
                User user = (User) arg;
                Map<String, Object> model = new HashMap<>();
                model.put("name", user.getName());
                model.put("url", url);
                try {
                    emailSender.sendEmail(user.getEmail(), "Ссылка на файл", model, "file_letter");
                } catch (MessagingException | TemplateException | IOException e) {
                    System.err.println("File url email sending exception.");
                }
                break;
            }
        }
    }
}
