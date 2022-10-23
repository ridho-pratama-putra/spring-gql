package com.example.springgql.controllers;

import com.example.springgql.models.Category;
import com.example.springgql.models.Transaction;
import com.example.springgql.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class GQLController {

    Logger logger = LoggerFactory.getLogger(GQLController.class);

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
                .build(), User.builder()
                .id(2L)
                .name("sumarna")
                .build()));
    }

//    @SchemaMapping(typeName = "User")
//    Flux<Transaction> transactions() throws ParseException {
//        logger.info("Transactions called when query need transactions to show");
//        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
//        return Flux.fromIterable(Arrays.asList(
//            new Transaction("krupuk", new Category("kebutuhan harian"), Double.parseDouble("1000"), date.parse("19/08/1945"))
//            , new Transaction("jipang", new Category("kebutuhan harian"), Double.parseDouble("2000"), date.parse("19/08/1945"))
//        ));
//    }

    @BatchMapping
    Map<User, List<Transaction>> transactions(List<User> users) {
        logger.info("trxs");
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        return users
                .stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> {
                            logger.info(user.toString());
                            try {
                                return Arrays.asList(
                                        new Transaction("krupuk", new Category("kebutuhan harian"), Double.parseDouble("1000"), date.parse("19/08/1945"))
                                        , new Transaction("jipang", new Category("kebutuhan harian"), Double.parseDouble("2000"), date.parse("19/08/1945"))
                                );
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }));
    }
}
