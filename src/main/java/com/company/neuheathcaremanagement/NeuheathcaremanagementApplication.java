package com.company.neuheathcaremanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NeuheathcaremanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeuheathcaremanagementApplication.class, args);
	}

}
