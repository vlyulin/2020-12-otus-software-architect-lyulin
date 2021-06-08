package ru.otus.spring.app.order.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"ru.otus.spring.app.billing.client.service","ru.otus.spring.app.notification.client.services"})
@EnableDiscoveryClient
public class OrderServiceMain extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OrderServiceMain.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceMain.class, args);
	}
}
