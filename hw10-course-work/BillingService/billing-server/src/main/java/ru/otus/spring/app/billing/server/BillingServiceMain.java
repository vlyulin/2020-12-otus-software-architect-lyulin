package ru.otus.spring.app.billing.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BillingServiceMain extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BillingServiceMain.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceMain.class, args);
	}
}
