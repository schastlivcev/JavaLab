package ru.kpfu.itis.rodsher.handlers;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kpfu.itis.rodsher.security.details.UserDetailsImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Log4j2
public class MappingErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(@AuthenticationPrincipal UserDetailsImpl userDetails, ModelMap map, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if(status != null) {
            Integer statusCode = Integer.parseInt(status.toString());
            map.put("status", statusCode);
            if(statusCode == 404) {
                log.warn("Request to unknown mapping: {}", uri);
                map.put("text", "Такой страницы не существует.");
            } else if(statusCode == 500) {
                log.error("Internal error occurred on request to: {} with parameters: {} and Exception[type={}, class={}, message: {}] in Servlet[name={}]",
                        uri,
                        request.getParameterMap(),
                        request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE),
                        request.getAttribute(RequestDispatcher.ERROR_EXCEPTION),
                        request.getAttribute(RequestDispatcher.ERROR_MESSAGE),
                        request.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME));
                map.put("text", "Произошел сбой при обработке запроса.");
            } else if(statusCode == 403) {
                if(userDetails != null) {
                    log.warn("Access to forbidden page: {} by User[id={}, name={}, surname={}]",
                            uri,
                            userDetails.getId(),
                            userDetails.getUser().getName(),
                            userDetails.getUser().getSurname());
                } else {
                    log.warn("Access to forbidden page: {} by anonymous User",
                            uri);
                }
                return "redirect:/";
            } else {
                log.error("Unhandled error occurred on request to: {} with parameters: {} with Exception[type={}, class={}, message={}] in Servlet[name={}]",
                        uri,
                        request.getParameterMap(),
                        request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE),
                        request.getAttribute(RequestDispatcher.ERROR_EXCEPTION),
                        request.getAttribute(RequestDispatcher.ERROR_MESSAGE),
                        request.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME));
                map.put("text", "Неизвестная ошибка.");
            }
        } else {
            if(userDetails != null) {
                log.error("Unacceptable request to: {} by User[id={},name={},surname={}]",
                        uri,
                        userDetails.getId(),
                        userDetails.getUser().getName(),
                        userDetails.getUser().getSurname());
            } else {
                log.error("Unacceptable request to: {} by anonymous User",
                        uri);
            }
            map.put("status", 400);
            map.put("text", "Недопустимый запрос.");
        }
        return "error_page";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
