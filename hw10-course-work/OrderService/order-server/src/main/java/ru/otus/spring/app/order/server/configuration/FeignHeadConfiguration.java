package ru.otus.spring.app.order.server.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.otus.spring.app.order.server.services.WebContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Configuration
public class FeignHeadConfiguration {
//    private final Logger logger = (java.util.logging.LoggerFactory) LoggerFactory.getLogger(this.getClass());
    private final Logger logger = Logger.getLogger(FeignHeadConfiguration.class.getName());

    @Autowired
    @Qualifier("OrderServiceWebContext")
    private WebContext webContext;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                // If you take the following method in the cookie
                Cookie[] cookies = request.getCookies();
                if (cookies != null && cookies.length > 0) {
                    for (Cookie cookie : cookies) {
                        requestTemplate.header(cookie.getName(), cookie.getValue());
                    }
                } else {
                    logger.warning("Failed to get a cookie!");
                }
                requestTemplate.header("x-userid", "101");
                logger.info("session_id "+webContext.getSession_id());
                requestTemplate.header(HttpHeaders.COOKIE,"session_id="+webContext.getSession_id());

                // If placed in the header by the following way
//                Enumeration<String> headerNames = request.getHeaderNames();
//                if (headerNames != null) {
//                    while (headerNames.hasMoreElements()) {
//                        String name = headerNames.nextElement();
//                        String value = request.getHeader(name);
//                        /**
//                         * Traverse the attribute field in the request header, add the jsessionid to the new request header and forward it to the downstream service.
//                         * */
//                        if ("jsessionid".equalsIgnoreCase(name)) {
//                            logger.info("Add custom request header key:" + name + ", value:" + value);
//                            requestTemplate.header(name, value);
//                        } else {
//                            logger.info("Non-custom request header key:" + name + ",value:" + value + "do not need to add!");
//                        }
//                    }
//                } else {
//                    logger.warning("Failed to get request header!");
//                }
            }
        };
    }
}
