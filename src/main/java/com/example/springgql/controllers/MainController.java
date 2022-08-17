package com.example.springgql.controllers;

import com.example.springgql.models.Transaction;
import com.example.springgql.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Controller
public class MainController {

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @QueryMapping
    public Mono<User> userById(@Argument int id) {
        return Mono.just(User.builder()
                .id(Long.valueOf(id))
                .name("sumarno")
                .build());
    }

    @QueryMapping
    public Flux<User> users() {
        return Flux.fromIterable(Arrays.asList(User.builder()
                .id(1L)
                .name("sumarno")
                .build(), User.builder()
                .id(2L)
                .name("sumarni")
                .build()));
    }

    @SchemaMapping(typeName = "User")
    Flux<Transaction> transactions() {
        logger.info("Transactions called when query need transactions to show");
        return Flux.fromIterable(Arrays.asList(
                new Transaction("krupuk", "kebutuhan harian", Double.parseDouble("1000")),
                new Transaction("jipang", "kebutuhan harian", Double.parseDouble("2000"))
        ));
    }
}
