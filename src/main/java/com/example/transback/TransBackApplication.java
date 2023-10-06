package com.example.transback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class TransBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransBackApplication.class, args);
	}
}

