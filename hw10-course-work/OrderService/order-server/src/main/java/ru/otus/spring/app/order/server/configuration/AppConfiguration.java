package ru.otus.spring.app.order.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.app.twopc.common.services.TwoPCTransactionManagerService;
import ru.otus.spring.app.twopc.common.services.TwoPCTransactionManagerServiceImpl;

// https://www.baeldung.com/spring-boot-add-filter
@Configuration
public class AppConfiguration {
//    @Bean
//    public FilterRegistrationBean<AuthenticationFilter> authFilter() {
//        FilterRegistrationBean<AuthenticationFilter> registrationBean =
//                new FilterRegistrationBean<>();
//        registrationBean.setFilter(new AuthenticationFilter());
//        return registrationBean;
//    }

    @Bean
    TwoPCTransactionManagerService getTwoPCResourceManagerService() {
        return new TwoPCTransactionManagerServiceImpl();
    }

}
