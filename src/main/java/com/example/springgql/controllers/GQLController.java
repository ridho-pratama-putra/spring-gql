package com.example.springgql.controllers;

import com.example.springgql.exception.DataNotDeletedException;
import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.logging.LoggingService;
import com.example.springgql.models.Release;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.DeletePayload;
import com.example.springgql.models.graphqlInput.ReleaseInput;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.services.ReleaseService;
import com.example.springgql.services.ArtistService;
import graphql.relay.Connection;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class GQLController {
    public final ArtistService artistService;
    public final ReleaseService releaseService;
    public final LoggingService log;

    GQLController(ArtistService artistService, ReleaseService releaseService, LoggingService log) {
        this.artistService = artistService;
        this.releaseService = releaseService;
        this.log = log;
    }

    @QueryMapping
    public Mono<Artist> artistById(@Argument(name = "id") String id) {
        if ("alex".equals(id)) {
            throw new DataNotFoundException(Artist.class);
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
    public Mono<Release> createReleaseOnArtist(@Argument ReleaseInput releaseInput) {
        return Mono.just(releaseService.saveReleaseOnArtist(releaseInput));
    }

//    @SchemaMapping(typeName = "Artist")
//    Flux<Album> albums(Artist artist) {
//        System.out.println("Transactions called when query need transactions to show");
//        return Flux.fromIterable(albumService.getAlbumsByArtistId(artist.getId()));
//    }

    @BatchMapping
    Map<Artist, List<Release>> releases(List<Artist> artists) {
        return releaseService.getReleasesByArtistIds(artists);
    }

    @QueryMapping
    Connection<Release> releasesByArtistId(@Argument(name = "id") String id, @Argument(name = "first") int first, @Argument(name = "after") String after) {
        return releaseService.getReleasesByArtistId(id, first, after);
    }

    @QueryMapping
    Connection<Release> allReleases(@Argument(name = "first") int first, @Argument(name = "after") String after) {
        return releaseService.getAllReleases(after, first);
    }

    @MutationMapping
    Mono<Release> updateRelease(@Argument(name = "id") String id, @Argument("releaseInput") ReleaseInput releaseInput) {
        Release release = releaseService.updateReleaseById(id, releaseInput);
        return Mono.just(release);
    }

    @MutationMapping
    Mono<Artist> updateArtist(@Argument(name = "id") String id, @Argument("artistInput") ArtistInput artistInput) {
        Artist artist = artistService.updateArtistById(id, artistInput);
        return Mono.just(artist);
    }

    @MutationMapping
    Mono<DeletePayload> deleteReleaseById(@Argument(name = "id") String id) {
        DeletePayload deletePayload = releaseService.deleteById(id);
        return Mono.just(deletePayload);
    }

    @MutationMapping
    Mono<DeletePayload> deleteArtistById(@Argument(name = "id") String id) {
        DeletePayload deletePayload = artistService.deleteById(id);
        return Mono.just(deletePayload);
    }
}
