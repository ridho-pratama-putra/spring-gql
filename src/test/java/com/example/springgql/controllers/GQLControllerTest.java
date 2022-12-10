package com.example.springgql.controllers;

import brave.Tracing;
import com.example.springgql.logging.LoggingService;
import com.example.springgql.models.Album;
import com.example.springgql.models.Artist;
import com.example.springgql.services.ArtistLibraryService;
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
import java.util.Arrays;

@GraphQlTest
@EnableAutoConfiguration
class GQLControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    ArtistLibraryService service;

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
        Mockito.when(tracer.nextSpan().context().spanId()).thenReturn("sfgagsd");
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
        Mockito.when(service.getAllArtist()).thenThrow(NullPointerException.class);
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
        Mockito.when(service.getAllArtist()).thenThrow(NullPointerException.class);
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
                "        id\n" +
                "   " +
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
    public void artist_shouldReturnListOfArtistWithTransaction_whenCalled() {
        Mockito.when(service.getAllArtist()).thenReturn(Arrays.asList(new Artist("ad", "add", Arrays.asList(new Album()))));
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
}