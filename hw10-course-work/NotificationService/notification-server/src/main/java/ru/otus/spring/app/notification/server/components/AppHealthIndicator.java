package ru.otus.spring.app.notification.server.components;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class AppHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        return Health.status("OK").build();
    }
}
