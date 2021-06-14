package ru.otus.spring.app.order.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.app.order.server.components.AuthenticationFilter;
import ru.otus.spring.app.order.server.services.WebContext;
import ru.otus.spring.app.twopc.common.services.TwoPCTransactionManagerService;
import ru.otus.spring.app.twopc.common.services.TwoPCTransactionManagerServiceImpl;

// https://www.baeldung.com/spring-boot-add-filter
@Configuration
public class AppConfiguration {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authFilter(@Autowired WebContext webContext) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean =
                new FilterRegistrationBean<>();

        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setWebContext(webContext);
        registrationBean.setFilter(authenticationFilter);
//        registrationBean.addUrlPatterns("/users");
//        registrationBean.addUrlPatterns("/user/*");
        return registrationBean;
    }

    @Bean
    TwoPCTransactionManagerService getTwoPCResourceManagerService() {
        return new TwoPCTransactionManagerServiceImpl();
    }

    @Bean(name="OrderServiceWebContext")
    WebContext getWebContextBean() {
        return new WebContext();
    }
}
