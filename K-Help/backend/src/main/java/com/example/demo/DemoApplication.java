package com.example.demo;

import com.example.demo.config.DatabaseUrlEnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DemoApplication.class);
		app.addListeners(new DatabaseUrlEnvironmentPostProcessor());
		app.run(args);
	}

}
