package com.example.springgql.controllers;

import com.example.springgql.models.Transaction;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class MainController {

    @QueryMapping
    public Flux<Transaction> transactions() {
        return Flux.fromArray(new Transaction[]{new Transaction("title1", "description1", Double.parseDouble("5000"))
                , new Transaction("title2", "description2", Double.parseDouble("10000"))});
    }
}
