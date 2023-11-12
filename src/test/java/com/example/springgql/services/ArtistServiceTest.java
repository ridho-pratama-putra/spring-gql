package com.example.springgql.services;

import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.repositories.ArtistRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ArtistServiceTest {

    @Autowired
    ArtistService service;

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
}