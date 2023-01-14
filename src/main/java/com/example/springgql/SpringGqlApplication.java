package com.example.springgql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.ZoneId;

@SpringBootApplication
@EnableMongoAuditing
public class SpringGqlApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringGqlApplication.class, args);
	}

	@Bean
	public Clock systemUtcClock() { // <--Note the method name will change the bean ID
		return Clock.system(ZoneId.of("Asia/Jakarta"));
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
