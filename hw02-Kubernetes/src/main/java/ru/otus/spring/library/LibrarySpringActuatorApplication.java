package ru.otus.spring.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// https://www.codebyamir.com/blog/how-to-deploy-spring-boot-war-to-tomcat
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
// @SpringBootApplication
public class LibrarySpringActuatorApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LibrarySpringActuatorApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(LibrarySpringActuatorApplication.class, args);
	}

}
