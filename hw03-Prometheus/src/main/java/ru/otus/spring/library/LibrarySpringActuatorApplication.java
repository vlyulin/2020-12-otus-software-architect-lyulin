package ru.otus.spring.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
//import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;

@SpringBootApplication
//@EnablePrometheusEndpoint
//@EnableSpringBootMetricsCollector
public class LibrarySpringActuatorApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LibrarySpringActuatorApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(LibrarySpringActuatorApplication.class, args);
	}

}
