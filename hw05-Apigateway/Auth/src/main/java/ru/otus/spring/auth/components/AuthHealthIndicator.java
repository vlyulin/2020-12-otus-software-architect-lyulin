package ru.otus.spring.auth.components;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class AuthHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        return Health.status("OK").build();
    }
}
