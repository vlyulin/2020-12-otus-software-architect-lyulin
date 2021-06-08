package ru.otus.spring.app.billing.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.app.twopc.common.services.TwoPCTransactionManagerService;
import ru.otus.spring.app.twopc.common.services.TwoPCTransactionManagerServiceImpl;

@Configuration
public class BillingServiceConfig {
    @Bean
    TwoPCTransactionManagerService getTwoPCResourceManagerService() {
        return new TwoPCTransactionManagerServiceImpl();
    }
}
