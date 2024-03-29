package com.example.springgql.controllers;

import com.example.springgql.enums.CategoryEnum;
import com.example.springgql.logging.LoggingService;
import com.example.springgql.models.Release;
import com.example.springgql.models.Artist;
import com.example.springgql.services.ReleaseService;
import com.example.springgql.services.ArtistService;
import com.example.springgql.utils.CursorUtil;
import graphql.relay.*;
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
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@GraphQlTest
@EnableAutoConfiguration
class GQLControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    ArtistService artistService;

    @MockBean
    ReleaseService releaseService;

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

    @MockBean
    private MappingMongoConverter mappingMongoConverter;

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
        Artist artist = Artist.builder().id("someValue").name("asdfg").build();
        Mockito.when(artistService.getArtistById(Mockito.any())).thenReturn(artist);
        Map<Artist, List<Release>> mockBatchMappingResult = new HashMap<>();
        mockBatchMappingResult.put(artist, Arrays.asList(Release.builder().title("sad").build()));
        Mockito.when(releaseService.getReleasesByArtistIds(Mockito.any())).thenReturn(mockBatchMappingResult);

        String request = "query artistById($id: ID){\n" +
                "    artistById(id: $id) {\n" +
                "     id\n" +
                "     name\n" +
                "        releases {\n" +
                "           title\n" +
                "        }\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .variable("id", "someValue")
                .execute()
                .path("artistById")
                .entity(Artist.class)
                .path("artistById.releases")
                .entityList(Release.class);
    }

    @Test
    public void artist_shouldReturnListOfArtistWithAlbum_whenCalled() {
        Mockito.when(artistService.getAllArtist()).thenReturn(Arrays.asList(new Artist("ad", "add", "display name")));
        Map<Artist, List<Release>> mockBatchMappingReult = new HashMap<>();
//        mockBatchMappingReult.put(Artist.builder().id("sad").name("saassa").build(), Arrays.asList(new Album(null, "title", CategoryEnum.ROCK, null, null, null, null)));
        Mockito.when(releaseService.getReleasesByArtistIds(Mockito.any())).thenReturn(mockBatchMappingReult);
        String request = "query {\n" +
                "    artists {\n" +
                "        name\n" +
                "        releases {\n" +
                "           title\n" +
                "        }\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .execute()
                .path("artists")
                .entityList(Artist.class)
                .path("artists.[*].albums")
                .entityList(Release.class)
        ;
        Mockito.verify(releaseService, Mockito.times(1)).getReleasesByArtistIds(Mockito.any());
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
    public void createReleaseOnArtist_shouldReturnCreatedAlbum_whenCalled() {
        Mockito.when(releaseService.saveReleaseOnArtist(Mockito.any())).thenReturn(Release.builder()
                .title("album1")
                .category(Collections.singletonList(CategoryEnum.ROCK))
                .releaseDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .build());
        Map<String, Object> variable = new HashMap<>();
        HashMap<String, String> artistInput = new HashMap<>();
        artistInput.put("name", "endank");
        variable.put("title", "album1");
        variable.put("releaseDate", "11/12/1997");
        variable.put("releaseType", "SINGLE");
        variable.put("category", "ROCK");
        variable.put("artist", artistInput);

        String request = "mutation($input: ReleaseInput!) {" +
                "  createReleaseOnArtist(releaseInput: $input) {" +
                "    title" +
                "    creationDate" +
                "    releaseDate" +
                "  }" +
                "}";

        graphQlTester.document(request)
                .variable("input", variable)
                .execute()
                .path("createReleaseOnArtist")
                .entity(Release.class)
                .path("createReleaseOnArtist.title")
                .entity(String.class)
        ;
    }

    @Test
    public void releasesByArtistId_shouldReturnSingleArtisWithAlbum_whenCalled() {
        ArrayList<Release> releases = new ArrayList<>();
        String firstData = "6396c71e80040f6cf5d85586";
        String secData = "639aa033b70a8f582f5b8b3b";
        String thirdData = "639ad946aedd867645eb75d9";
        String fourthData = "639ad9e1aedd867645eb75de";
        int first = 2;
        String after = secData;
        releases.add(Release.builder().id(firstData).title("brong").build());
        releases.add(Release.builder().id(secData).title("brong").build());
        releases.add(Release.builder().id(thirdData).title("brong").build());
        releases.add(Release.builder().id(fourthData).title("brong").build());
        List<Edge<Release>> defaultEdges = releases
                .stream()
                .map(release -> new DefaultEdge<Release>(release, CursorUtil.convertCursorFromId(release.getId())))
                .limit(first)
                .collect(Collectors.toList());

        ConnectionCursor startCursor = CursorUtil.getConnectionCursor(defaultEdges, 0);
        ConnectionCursor endCursor = CursorUtil.getConnectionCursor(defaultEdges, defaultEdges.size() - 1);
        DefaultPageInfo defaultPageInfo = new DefaultPageInfo(
                startCursor,
                endCursor,
                after != null,
                defaultEdges.size() >= first
        );

        Mockito.when(releaseService.getReleasesByArtistId(Mockito.any(String.class), Mockito.any(int.class), Mockito.any(String.class))).thenReturn(new DefaultConnection<>(defaultEdges, defaultPageInfo));
        String request = "query releasesByArtistId($id: ID, $first: Int, $after: String){\n" +
                "    releasesByArtistId(id: $id, first: $first, after: $after) {\n" +
                "        edges {\n" +
                "            cursor\n" +
                "            node {\n" +
                "                title\n" +
                "            }\n" +
                "        }\n" +
                "        pageInfo {\n" +
                "           hasPreviousPage\n" +
                "           hasNextPage\n" +
                "           startCursor\n" +
                "           endCursor\n" +
                "        }\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .variable("id", "someValue")
                .variable("first", first)
                .variable("after", after)
                .execute()
                .path("releasesByArtistId.edges[*].node")
                .entityList(Release.class)
                .hasSize(2);
    }

    @Test
    public void allReleases_shouldReturnEmptyConnections_whenCalled() {
        int first = 2;
        String firstData = "6396c71e80040f6cf5d85586";
        String secData = "639aa033b70a8f582f5b8b3b";
        String thirdData = "639ad946aedd867645eb75d9";
        String fourthData = "639ad9e1aedd867645eb75de";
        String after = secData;

        String request = "query allReleases($first: Int, $after: String){\n" +
                "    allReleases(first: $first, after: $after) {\n" +
                "        edges {\n" +
                "            cursor\n" +
                "            node {\n" +
                "                id\n" +
                "                title\n" +
                "            }\n" +
                "        }\n" +
                "        pageInfo {\n" +
                "           hasPreviousPage\n" +
                "           hasNextPage\n" +
                "           startCursor\n" +
                "           endCursor\n" +
                "        }\n" +
                "    }\n" +
                "}";
        ArrayList<Release> releases = new ArrayList<>();
        releases.add(Release.builder().id(firstData).title("brong").build());
        releases.add(Release.builder().id(secData).title("brong").build());
        releases.add(Release.builder().id(thirdData).title("brong").build());
        releases.add(Release.builder().id(fourthData).title("brong").build());
        List<Edge<Release>> defaultEdges = releases
                .stream()
                .map(release -> new DefaultEdge<Release>(release, CursorUtil.convertCursorFromId(release.getId())))
                .limit(first)
                .collect(Collectors.toList());
        ConnectionCursor startCursor = CursorUtil.getConnectionCursor(defaultEdges, 0);
        ConnectionCursor endCursor = CursorUtil.getConnectionCursor(defaultEdges, defaultEdges.size() - 1);
        DefaultPageInfo defaultPageInfo = new DefaultPageInfo(
                startCursor,
                endCursor,
                after != null,
                defaultEdges.size() >= first
        );
        Mockito.when(releaseService.getAllReleases(Mockito.anyString(), Mockito.anyInt())).thenReturn(new DefaultConnection<>(defaultEdges, defaultPageInfo));

        graphQlTester.document(request)
                .variable("first", 2)
                .variable("after", firstData)
                .execute()
                .path("allReleases.edges[*].node")
                .entityList(Release.class)
                .hasSize(2)
        ;
    }
}