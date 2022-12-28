package com.example.springgql.controllers;

import com.example.springgql.enums.CategoryEnum;
import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.logging.LoggingService;
import com.example.springgql.models.Album;
import com.example.springgql.models.Artist;
import com.example.springgql.services.AlbumService;
import com.example.springgql.services.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@GraphQlTest
@EnableAutoConfiguration
class GQLControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    ArtistService artistService;

    @MockBean
    AlbumService albumService;

    @MockBean
    LoggingService loggingService;

    @MockBean
    HttpServletRequest httpServletRequest;

    @MockBean
    HttpServletResponse httpServletResponse;

    @MockBean
    Tracer tracer;

    @MockBean
    Span span;

    @MockBean
    TraceContext traceContext;

    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    public void init() {
        Mockito.when(tracer.nextSpan()).thenReturn(span);
        Mockito.when(tracer.nextSpan().context()).thenReturn(traceContext);
    }

    @Test
    public void artist_shouldReturnListOfArtist_whenCalled() {
        String request = "query {\n" +
                "    artists {\n" +
                "        name\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .execute()
                .path("artists")
                .entityList(Artist.class);
    }

    @Test
    public void artistByid_shouldReturnSingleArtist_whenCalled() {
        Mockito.when(artistService.getArtistById(Mockito.any())).thenReturn(Artist.builder().id("ads").name("sd").build());
                String request = "query {\n" +
                "    artistById(id: 1) {\n" +
                "        name\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .execute()
                .path("artistById")
                .entity(Artist.class);
    }

    @Test
    public void artist_shouldReturnErrorInBody_whenServiceReturnException() {
        Mockito.when(artistService.getAllArtist()).thenThrow(NullPointerException.class);
        String request = "query {\n" +
                "    artists {\n" +
                "        name\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .execute()
                .errors();
    }

    @Test
    public void artist_shouldReturnErrorInBody_whenQueryTypo() {
        Mockito.when(artistService.getAllArtist()).thenThrow(NullPointerException.class);
        String request = "query {\n" +
                "    artiss {\n" +
                "        name\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .execute()
                .errors();
    }

    @Test
    public void artistById_shouldReturnSingleArtisWithAlbum_whenCalled() {
        String request = "query artistById($id: ID){\n" +
                "    artistById(id: $id) {\n" +
                "     id\n" +
                "     name\n" +
                "        albums {\n" +
                "           title\n" +
                "        }\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .variable("id", "someValue")
                .execute()
                .path("artistById")
                .entity(Artist.class)
                .path("artistById.albums")
                .entityList(Album.class);
    }

    @Test
    public void artist_shouldReturnListOfArtistWithAlbum_whenCalled() {
        Mockito.when(artistService.getAllArtist()).thenReturn(Arrays.asList(new Artist("ad", "add")));
        Mockito.when(albumService.getAlbumsByArtistId(Mockito.any())).thenReturn(Arrays.asList(new Album(null, "title", CategoryEnum.ROCK, null, null, null, null, null)));
        String request = "query {\n" +
                "    artists {\n" +
                "        name\n" +
                "        albums {\n" +
                "           title\n" +
                "        }\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .execute()
                .path("artists")
                .entityList(Artist.class)
                .path("artists.[0].albums")
                .entityList(Album.class)
        ;
    }

    @Test
    public void createArtist_shouldReturnCreatedArtist_whenCalled() {
        Mockito.when(artistService.saveArtistInput(Mockito.any())).thenReturn(Artist.builder()
                .name("endank")
                .build());
        HashMap<String, String> artistInput = new HashMap<>();
        artistInput.put("name", "endank");

        String request = "mutation($input: ArtistInput!) {" +
                "  createArtist(artistInput: $input) {" +
                "    name" +
                "  }" +
                "}";

        graphQlTester.document(request)
                .variable("input", artistInput)
                .execute()
                .path("createArtist")
                .entity(Artist.class)
                .path("createArtist.name")
                .entity(String.class)
        ;
    }

    @Test
    public void createAlbumOnArtist_shouldReturnCreatedAlbum_whenCalled() {
        Mockito.when(albumService.saveAlbumOnArtist(Mockito.any())).thenReturn(Album.builder()
                .title("album1")
                .addedDate(new Date())
                .categoryEnum(CategoryEnum.ROCK)
                .releaseDate("11/12/1997")
                .build());
        Map<String, Object> variable = new HashMap<>();
        HashMap<String, String> artistInput = new HashMap<>();
        artistInput.put("name", "endank");
        variable.put("title", "album1");
        variable.put("releaseDate", "11/12/1997");
        variable.put("artist", artistInput);

        String request = "mutation($input: AlbumInput!) {" +
                "  createAlbumOnArtist(albumInput: $input) {" +
                "    title" +
                "    releaseDate" +
                "  }" +
                "}";

        graphQlTester.document(request)
                .variable("input", variable)
                .execute()
                .path("createAlbumOnArtist")
                .entity(Album.class)
                .path("createAlbumOnArtist.title")
                .entity(String.class)
        ;
    }

    @Test
    public void albumsByArtistId_shouldReturnSingleArtisWithAlbum_whenCalled() {
        Mockito.when(albumService.getAlbumsByArtistId(Mockito.any())).thenReturn(Arrays.asList(new Album(null, "tutel", null, null, null, null, null, null)));
        String request = "query albumsByArtistId($id: ID){\n" +
                "    albumsByArtistId(id: $id) {\n" +
                "        title\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .variable("id", "someValue")
                .execute()
                .path("albumsByArtistId")
                .entityList(Album.class)
                .path("albumsByArtistId[0].title")
                .entity(String.class);
    }
}