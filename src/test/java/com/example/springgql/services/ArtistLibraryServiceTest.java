package com.example.springgql.services;

import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.repositories.ArtistRepository;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@SpringBootTest
class ArtistLibraryServiceTest {

    private static final String CONNECTION_STRING = "mongodb://%s:%d";
    private MongodExecutable mongodExecutable;

    @Autowired
    ArtistLibraryService service;

    @MockBean
    ArtistRepository repository;

    @AfterEach
    void clean() {
        mongodExecutable.stop();
    }

    @BeforeEach
    void setup() throws Exception {
        String ip = "localhost";
        int port = 27017;

        ImmutableMongodConfig mongodConfig = MongodConfig.builder().version(Version.Main.PRODUCTION).net(new Net(ip, port, Network.localhostIsIPv6())).build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
    }

    @Test
    public void saveArtist_shouldReturnCreatedArtist_whenSuccessCreate() {
        ArtistInput input = new ArtistInput("springGQLArtist");
        Artist actualResult = service.saveArtist(input);

        Assertions.assertEquals("springGQLArtist", actualResult.getName());
    }

    @Test
    public void getAllArtist_shouldHaveLengthOne_whenOnlyOneDataExist() {
        ArtistInput input = new ArtistInput("springGQLArtist");
        service.saveArtist(input);

        service.getAllArtist();

        List<Artist> collection = repository.findAll();
        Assertions.assertEquals(1, collection.size());
    }
}