package ru.kpfu.itis.rodsher.filters;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.util.WebUtils;
import ru.kpfu.itis.rodsher.dto.Dto;
import ru.kpfu.itis.rodsher.dto.Status;
import ru.kpfu.itis.rodsher.services.UsersService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

@Component("authorizationFilter")
public class AuthorizationFilter implements Filter {
    @Autowired
    private UsersService usersService;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String context = req.getServletContext().getContextPath();
        String url = req.getRequestURI();
        HttpSession session = req.getSession();
        if(session.getAttribute("user") != null) {
            // Forbidden urls for authenticated
            if(url.matches(context + "/signIn/?") || url.matches(context + "/signUp/?.*")) {
                resp.sendRedirect("/");
                chain.doFilter(req, resp);
                return;
            }
            // Token verification
            Cookie cookie = WebUtils.getCookie(req, "Authorization");
            if(cookie != null && cookie.getValue().equals(session.getAttribute("token"))) {
                chain.doFilter(req, resp);
            } else {
                resp.sendRedirect("/signIn");
            }
            return;
        } else {
            // Authentication
            if(url.matches(context + "/signIn/?") && req.getMethod().equals("POST")) {
                Dto dto = usersService.signIn(req.getParameter("email"), req.getParameter("password"));
                if(dto.getStatus().equals(Status.USER_SIGN_IN_SUCCESS)) {
                    resp.addCookie(new Cookie("Authorization", (String) dto.get("token")));
                    session.setAttribute("user", dto.get("user"));
                    session.setAttribute("token", dto.get("token"));
                    resp.sendRedirect("/");
                    return;
                } else {
                    try {
                        Configuration templateGetter = freeMarkerConfig.getConfiguration();
                        templateGetter.setEncoding(Locale.getDefault(), "UTF-8");
                        Template template = templateGetter.getTemplate( "auth/sign_in.ftl");
                        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,
                                Collections.singletonMap("errors", Collections.singletonList("No such user")));
                        resp.getWriter().print(html);
                        return;
                    } catch (TemplateException e) {
                        throw new IllegalStateException("'Sign in' View filter exception", e);
                    }

                }
            }
            // Get Authentication pages
            if(url.matches(context + "/signIn/?") || url.matches(context + "/signUp/?.*")
                    || url.matches(context + "/?")) {
                chain.doFilter(req, resp);
            } else {
                resp.sendRedirect("/signIn");
            }
        }
    }
}