package com.example.springgql.services;

import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.models.Album;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.AlbumInput;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.repositories.AlbumRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AlbumServiceTest {

    @MockBean
    AlbumRepository repository;

    @MockBean
    ArtistService artistService;

    @Autowired
    AlbumService service;

    @Test
    public void saveAlbumOnArtist_shouldReturnEntity_whenSuccessSaveArtistNameExist() {
        Artist artist = Artist.builder()
                .name("endank")
                .build();
        Album album = Album.builder()
                .title("juara")
                .build();
        Mockito.when(artistService.getArtistByName(Mockito.any())).thenReturn(artist);
        Mockito.when(repository.save(Mockito.any())).thenReturn(album);
        AlbumInput juara = AlbumInput.builder()
                .title("juara")
                .artist(ArtistInput.builder().name("endank").build())
                .releaseDate("13/12/2012")
                .build();

        Album actualResult = service.saveAlbumOnArtist(juara);

        Assertions.assertEquals("juara", actualResult.getTitle());
    }

    @Test
    public void saveAlbumOnArtist_shouldReturnExceptionDataNotFoundException_whenArtistNotFound() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            AlbumInput juara = AlbumInput.builder()
                    .artist(ArtistInput.builder().name("endank").build())
                    .build();
            Mockito.when(artistService.getArtistByName(Mockito.any())).thenReturn(null);
            service.saveAlbumOnArtist(juara);
        });
    }

    @Test
    public void getAlbumsByArtistId_shouldReturnEntity_whenDataIsExist() {
        String artistId = "uniqueArtistId";
        ArrayList<Album> albums = new ArrayList<>();
        albums.add(new Album());
        Mockito.when(repository.findAllByArtistId(Mockito.any())).thenReturn(albums);

        List<Album> actualResult = service.getAlbumsByArtistId(artistId);
        Assertions.assertNotNull(actualResult);
    }

    @Test
    public void getAlbumsByArtistId_shouldReturnDataNotFoundException_whenDataIsNotExist() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            String artistId = "uniqueArtistId";
            ArrayList<Album> albums = new ArrayList<>();
            Mockito.when(repository.findAllByArtistId(Mockito.any())).thenReturn(albums);

            service.getAlbumsByArtistId(artistId);
        });
    }
}