package ru.otus.spring.app.order.server.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;
import ru.otus.spring.app.order.server.services.WebContext;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

public class AuthenticationFilter extends GenericFilterBean {

    public static final String SESSION_ID_COOKIE = "session_id";
    public static final String X_USER_ID = "X-UserId";
    private WebContext webContext;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        //
        //        // Each header field consists of a case-insensitive field name
        // https://tools.ietf.org/html/rfc7230#section-3.2

        Cookie session_id_cookie = WebUtils.getCookie(httpRequest, SESSION_ID_COOKIE);
        String session_id = httpRequest.getHeader(SESSION_ID_COOKIE);
        logger.info("Set header session_id = " + session_id);
        String xUserdId = httpRequest.getHeader(X_USER_ID);
        logger.info("x-userid = " + xUserdId);
        String path = httpRequest.getRequestURI();
        if(session_id_cookie != null || xUserdId != null || path.startsWith("/health")) {
            logger.info("Pass security");
            if(session_id_cookie != null) {
                logger.info("Set session_id = " + session_id_cookie.getValue());
                webContext.setSession_id(session_id_cookie.getValue());
            }
            if(session_id != null) {
                logger.info("Set header session_id = " + session_id);
                webContext.setSession_id(session_id);
            }
            chain.doFilter(request, response);
            return;
        }
        logger.info("Not authenticated");
        httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Not authenticated");
    }

    public void setWebContext(WebContext webContext) {
        this.webContext = webContext;
    }
}
