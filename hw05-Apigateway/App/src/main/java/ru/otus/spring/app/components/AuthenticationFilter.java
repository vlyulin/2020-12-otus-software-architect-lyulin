package ru.otus.spring.app.components;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter extends GenericFilterBean { // implements Filter {
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
//        Enumeration<String> headerNames = httpRequest.getHeaderNames();
//        while(headerNames.hasMoreElements()) {
//            String xHeaderName = headerNames.nextElement();
//            // Each header field consists of a case-insensitive field name
//            // https://tools.ietf.org/html/rfc7230#section-3.2
//            if("x-userid".equalsIgnoreCase(xHeaderName)) {
//                chain.doFilter(request, response);
//                return;
//            }
//        }
        httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Not authenticated");
    }
}
