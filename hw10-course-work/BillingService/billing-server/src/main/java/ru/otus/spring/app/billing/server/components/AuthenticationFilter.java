package ru.otus.spring.app.billing.server.components;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Each header field consists of a case-insensitive field name
        // https://tools.ietf.org/html/rfc7230#section-3.2

        String xUserdId = httpRequest.getHeader("x-userid");
        if(xUserdId != null) {
            chain.doFilter(request, response);
            return;
        }
        httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Not authenticated");
    }
}
