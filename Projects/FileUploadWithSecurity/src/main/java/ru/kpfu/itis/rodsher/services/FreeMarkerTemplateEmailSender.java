package ru.kpfu.itis.rodsher.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Component
public class FreeMarkerTemplateEmailSender {
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, Map attributes, String templateName) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setSubject(subject);
        helper.setTo(to);

        Configuration templateGetter = freeMarkerConfig.getConfiguration();
        templateGetter.setEncoding(Locale.getDefault(), "UTF-8");
        Template template = templateGetter.getTemplate(templateName + ".ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, attributes);

        helper.setText(html, true);
        mailSender.send(helper.getMimeMessage());
    }
}
