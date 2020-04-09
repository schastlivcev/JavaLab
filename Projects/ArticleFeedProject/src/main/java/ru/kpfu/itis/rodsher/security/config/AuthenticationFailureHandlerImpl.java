package ru.kpfu.itis.rodsher.security.config;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AuthenticationFailureHandlerImpl extends ExceptionMappingAuthenticationFailureHandler {
    private FreeMarkerConfig freeMarkerConfig;

    public AuthenticationFailureHandlerImpl(FreeMarkerConfig freeMarkerConfig) {
        this.freeMarkerConfig = freeMarkerConfig;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
        req.setAttribute("email", req.getParameter("email"));
        Map<String, Object> map = new HashMap<>();
        if(e instanceof UsernameNotFoundException) {
            if(e.getMessage().equals("User not found.")) {
                map.put("error", "USER_NOT_FOUND");
            } else if(e.getMessage().equals("Email is empty.")) {
                map.put("error", "EMAIL_EMPTY");
            } else if(e.getMessage().equals("Unacceptable email format.")) {
                map.put("error", "EMAIL_UNACCEPTABLE");
            } else {
                map.put("error", "DATA_UNACCEPTABLE");
            }
        } else {
            map.put("error", "DATA_UNACCEPTABLE");
        }
        try {
            Template template = freeMarkerConfig.getConfiguration().getTemplate("auth/sign_in.ftl", Locale.getDefault(), "UTF-8", true);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            resp.getWriter().print(html);
        } catch (TemplateException ex) {
            ex.printStackTrace();
//            throw new ServletException("FreeMarker template processing failure.", e);
        }
//        resp.sendError(405);
    }
}
