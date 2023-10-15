package com.example.transback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TransBackApplication {
	public static void main(String[] args) {
		SpringApplication.run(TransBackApplication.class, args);
	}

}

