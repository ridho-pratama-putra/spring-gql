package com.example.springgql.services;

import com.example.springgql.exception.DataNotDeletedException;
import com.example.springgql.models.Artist;
import com.example.springgql.models.Release;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.repositories.ArtistRepository;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
class ArtistServiceTest {

    @Autowired
    ArtistService service;
    @MockBean
    ReleaseService releaseService;

    @MockBean
    ArtistRepository repository;

    @Test
    public void saveArtistInput_shouldReturnCreatedArtist_whenSuccessCreate() {
        String expectedName = "springGQLArtist";
        Mockito.when(repository.save(Mockito.any())).thenReturn(Artist.builder().name(expectedName).build());

        ArtistInput input = ArtistInput.builder()
        .name(expectedName)
        .build();

        Artist actualResult = service.saveArtistInput(input);

        Assertions.assertEquals(expectedName, actualResult.getName());
    }

    @Test
    public void getAllArtist_shouldHaveLengthOne_whenOnlyOneDataExist() {
        String expectedName = "springGQLArtist";
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(Artist.builder().name(expectedName).build()));

        List<Artist> actualResult = service.getAllArtist();

        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(expectedName, actualResult.get(0).getName());
    }

    @Test
    @Ignore
    void deleteById_shouldThrowFailedToDelete_whenArtistHaveSomeReleases() {
        Assertions.assertThrows(DataNotDeletedException.class, () -> {
            Artist artist = Artist.builder().name("endank").build();
            Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(artist));
            Release release = Release.builder().artist(artist).title("happy birthday!").build();
            Map<Artist, List<Release>> unDeletedRelease = new HashMap<>();
            unDeletedRelease.put(artist, Arrays.asList(release));
            Mockito.when(releaseService.getReleasesByArtistIds(Mockito.anyList())).thenReturn(unDeletedRelease);

            service.deleteById("some id matched with deleted record");
        });
    }
}