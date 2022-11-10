package com.example.springgql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SpringGqlApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringGqlApplication.class, args);
	}
}
