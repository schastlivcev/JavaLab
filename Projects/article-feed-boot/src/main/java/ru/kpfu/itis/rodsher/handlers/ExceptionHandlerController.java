package ru.kpfu.itis.rodsher.handlers;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
@Log4j2
public class ExceptionHandlerController {

    @ExceptionHandler({SQLException.class, DataAccessException.class, PersistenceException.class})
    public ModelAndView databaseError(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest req, Exception ex) {
        logError("Database error", userDetails, req, ex);
        ModelAndView view = new ModelAndView();
        view.addObject("status", 500);
        view.addObject("text", "Произошел сбой при работе с базой данных.");
        view.setViewName("error_page");
        return view;
    }

    @ExceptionHandler({IllegalStateException.class})
    public ModelAndView internalError(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest req, Exception ex) {
        logError("Illegal application state. Internal error", userDetails, req, ex);
        ModelAndView view = new ModelAndView();
        view.addObject("status", 400);
        view.addObject("text", "Недопустимый запрос.");
        view.setViewName("error_page");
        return view;
    }

    @ExceptionHandler({InterruptedException.class, IllegalThreadStateException.class})
    public ModelAndView threadError(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest req, Exception ex) {
        logError("Thread state error", userDetails, req, ex);
        ModelAndView view = new ModelAndView();
        view.addObject("status", 500);
        view.addObject("text", "Произошел сбой при связи с другими клиентами.");
        view.setViewName("error_page");
        return view;
    }

    @ExceptionHandler({IllegalArgumentException.class, ArrayIndexOutOfBoundsException.class, NumberFormatException.class})
    public ModelAndView dataFormatError(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest req, Exception ex) {
        logError("Request data format error", userDetails, req, ex);
        ModelAndView view = new ModelAndView();
        view.addObject("status", 500);
        view.addObject("text", "Недопустимый формат данных.");
        view.setViewName("error_page");
        return view;
    }

    @ExceptionHandler({IOException.class})
    public ModelAndView ioError(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest req, Exception ex) {
        logError("IO handling error", userDetails, req, ex);
        ModelAndView view = new ModelAndView();
        view.addObject("status", 500);
        view.addObject("text", "Произошел сбой доступа к данным.");
        view.setViewName("error_page");
        return view;
    }

    private void logError(String messageStartsWith, UserDetailsImpl userDetails, HttpServletRequest req, Exception ex) {
        if(userDetails != null) {
            log.error(messageStartsWith + " occurred on request to: {} by User[id={}, name={}, surname={}] with parameters: {} and Exception[class={}, message={}]",
                    req.getRequestURI(),
                    userDetails.getId(), userDetails.getUser().getName(), userDetails.getUser().getSurname(),
                    req.getParameterMap(),
                    ex.getClass().getName(), ex.getMessage());
        } else {
            log.error(messageStartsWith + " occurred on request to: {} by anonymous User with parameters: {} and Exception[class={}, message={}]",
                    req.getRequestURI(),
                    req.getParameterMap(),
                    ex.getClass().getName(), ex.getMessage());
        }
    }
}