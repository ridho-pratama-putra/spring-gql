package com.example.springgql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.ZoneId;

@SpringBootApplication
public class SpringGqlApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringGqlApplication.class, args);
	}

	@Bean
	public Clock systemUtcClock() { // <--Note the method name will change the bean ID
		return Clock.system(ZoneId.of("Asia/Jakarta"));
	}
}
