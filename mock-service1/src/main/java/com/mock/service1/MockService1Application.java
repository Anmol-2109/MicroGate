package com.mock.service1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MockService1Application {

	public static void main(String[] args) {
		SpringApplication.run(MockService1Application.class, args);
	}

}
