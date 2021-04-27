package ru.otus.spring.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

//@SpringBootApplication (scanBasePackages = "ru.otus.spring.*")
@SpringBootApplication
public class AuthService extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AuthService.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthService.class, args);
	}
}
