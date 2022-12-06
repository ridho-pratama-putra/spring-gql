package com.example.springgql.services;

import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.repositories.ArtistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class ArtistLibraryServiceTest {

    @Autowired
    ArtistLibraryService service;

    @MockBean
    ArtistRepository repository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void saveArtist_shouldReturnCreatedArtist_whenSuccessCreate() {
        String expectedName = "springGQLArtist";
        Mockito.when(repository.save(Mockito.any())).thenReturn(Artist.builder().name(expectedName).build());
        ArtistInput input = new ArtistInput(expectedName);
        Mockito.when(restTemplate.postForEntity(Mockito.contains("/album"), Mockito.any(), Mockito.any())).thenReturn(null);

        Artist actualResult = service.saveArtist(input);

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