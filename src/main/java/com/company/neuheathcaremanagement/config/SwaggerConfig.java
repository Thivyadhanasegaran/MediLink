package com.company.neuheathcaremanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


// http://localhost:8080/swagger-ui/index.html#/


@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("NEU Student Healthcare Management System API").version("1.0")
				.description("This API provides functionalities for the NEU Student Healthcare Management System. "
						+ "It includes features like user authentication, health record management, appointment scheduling, "
						+ "prescription handling and notifications. "
						+ "This service is built using Spring Boot and SpringDoc OpenAPI for documenting and visualizing the APIs."));
	}
}
