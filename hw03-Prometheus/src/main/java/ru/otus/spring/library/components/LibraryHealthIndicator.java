package ru.otus.spring.library.components;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

// localhost:8080/actuator/health/library
@Component
public class LibraryHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        return Health.status("OK").build();
//        return Health.up().withDetail("message", "Библиотека открыта.").build();
    }
}
