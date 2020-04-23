package ru.kpfu.itis.rodsher.websockets.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import java.util.Map;

@Component
public class WebSocketAuthorizationHandshakeHandler implements HandshakeHandler {

    private DefaultHandshakeHandler handshakeHandler = new DefaultHandshakeHandler();

    @Override
    public boolean doHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Map<String, Object> map) throws HandshakeFailureException {
        ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
        Cookie cookie = WebUtils.getCookie(request.getServletRequest(), "Authorization");
        if(cookie != null && cookie.getValue().equals(request.getServletRequest().getSession().getAttribute("token"))) {
            return handshakeHandler.doHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, map);
        }
        serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
        return false;
    }
}