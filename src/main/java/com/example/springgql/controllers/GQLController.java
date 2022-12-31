package com.example.springgql.controllers;

import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.logging.LoggingService;
import com.example.springgql.models.Album;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.AlbumInput;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.services.AlbumService;
import com.example.springgql.services.ArtistService;
import graphql.relay.Connection;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Controller
public class GQLController {
    public final ArtistService artistService;
    public final AlbumService albumService;
    public final LoggingService log;

    GQLController(ArtistService artistService, AlbumService albumService, LoggingService log) {
        this.artistService = artistService;
        this.albumService = albumService;
        this.log = log;
    }

    @QueryMapping
    public Mono<Artist> artistById(@Argument(name = "id") String id) {
        if ("alex".equals(id)) {
            throw new DataNotFoundException("Data not found");
        }
        return Mono.just(artistService.getArtistById(id));
    }

    @QueryMapping
    public Flux<Artist> artists() {
        List<Artist> allArtist = artistService.getAllArtist();
        return Flux.fromIterable(allArtist);
    }

    @MutationMapping
    public Mono<Artist> createArtist(@Argument ArtistInput artistInput) {
        Artist artist = artistService.saveArtistInput(artistInput);
        return Mono.just(artist);
    }

    @MutationMapping
    public Mono<Album> createAlbumOnArtist(@Argument AlbumInput albumInput) {
        return Mono.just(albumService.saveAlbumOnArtist(albumInput));
    }

//    @SchemaMapping(typeName = "Artist")
//    Flux<Album> albums(Artist artist) {
//        System.out.println("Transactions called when query need transactions to show");
//        return Flux.fromIterable(albumService.getAlbumsByArtistId(artist.getId()));
//    }

    @BatchMapping
    Map<Artist, List<Album>> albums(List<Artist> artists) {
        return albumService.getAlbumsByArtistIds(artists);
    }

    @QueryMapping
    Flux<Album> albumsByArtistId(@Argument(name = "id") String id) {
        return Flux.fromIterable(albumService.getAlbumsByArtistId(id));
    }

    @QueryMapping
    Connection<Album> allAlbums(@Argument(name = "first") int first, @Argument(name = "after") String after) {
        return albumService.getAllAlbums(after, first);
    }
}
