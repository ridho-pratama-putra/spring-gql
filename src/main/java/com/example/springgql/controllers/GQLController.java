package com.example.springgql.controllers;

import com.example.springgql.enums.CategoryEnum;
import com.example.springgql.models.Album;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.services.ArtistLibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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
    private static final Logger logger = LoggerFactory.getLogger(GQLController.class);

    ArtistLibraryService service;

    GQLController(ArtistLibraryService service) {
        this.service = service;
    }

    @QueryMapping
    public Mono<Artist> artistById(@Argument String id) {
        return Mono.just(Artist.builder().id(id).name("sumarno").build());
    }

    @QueryMapping
    public Flux<Artist> artists() {
        logger.info("query get all artist {} ", GQLController.class);
        List<Artist> allArtist = service.getAllArtist();
        return Flux.fromIterable(allArtist);
    }

    @MutationMapping
    public Mono<Artist> createArtist(@Argument ArtistInput artistInput) {
        Artist artist = service.saveArtist(artistInput);
        return Mono.just(artist);
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
    Map<Artist, List<Album>> albums(List<Artist> artists) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        return artists.stream().collect(Collectors.toMap(artist -> artist, artist -> {
            try {
                return Arrays.asList(new Album("krupuk", "rambak", CategoryEnum.ROCK, date.parse("19/08/1945")), new Album("jipang", "stroberi", CategoryEnum.POP, date.parse("19/08/1945")));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
