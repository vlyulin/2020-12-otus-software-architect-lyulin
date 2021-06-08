package ru.otus.spring.app.notification.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class NotificationServiceMain extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NotificationServiceMain.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceMain.class, args);
	}
}
