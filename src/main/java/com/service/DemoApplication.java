package com.service;

import com.domain.domaincontroller.DomainController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	private static DomainController domainController;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}



}
