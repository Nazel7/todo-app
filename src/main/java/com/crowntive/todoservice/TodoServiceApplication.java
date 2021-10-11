package com.crowntive.todoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.crowntive")
public class TodoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoServiceApplication.class, args);
	}

}
