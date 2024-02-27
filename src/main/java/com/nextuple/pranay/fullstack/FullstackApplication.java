package com.nextuple.pranay.fullstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class FullstackApplication {
	public static void main(String[] args) {
		SpringApplication.run(FullstackApplication.class, args);
		System.out.println("Server Started...");
	}

}
