package ru.otus.spring.app.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.app.components.AuthenticationFilter;

// https://www.baeldung.com/spring-boot-add-filter
@Configuration
public class AppConfiguration {
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean =
                new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/users");
        registrationBean.addUrlPatterns("/user/*");
        return registrationBean;
    }
}
