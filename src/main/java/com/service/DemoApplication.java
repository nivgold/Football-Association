package com.service;

import com.domain.domaincontroller.ServiceLayerManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	private static ServiceLayerManager serviceLayerManager;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}



}
