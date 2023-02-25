package com.example.springgql.services;

import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.models.Album;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.AlbumInput;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.repositories.AlbumRepository;
import graphql.relay.DefaultConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

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
        String fakeID = "63c911b6a272812098224d7c";
        ArrayList<Album> albums = new ArrayList<>();
        albums.add(Album.builder().id(fakeID).build());
        Mockito.when(repository.findAllByArtistId(Mockito.any(), Mockito.any())).thenReturn(albums);

        DefaultConnection<Album> albumsByArtistId = service.getAlbumsByArtistId(fakeID, 1, "");
        Assertions.assertNotNull(albumsByArtistId);
    }

    @Test
    public void getAlbumsByAfterCursor_shouldCallfindAllByIdGreaterThan_whenAfterCursorIsNotEmptyValue() {
        service.getAlbumsByAfterCursor("NjNhOWIyYzY5MzRhNzk3ZGZlMzMyMmY5", 2);

        Mockito.verify(repository, Mockito.times(1)).findAllByIdGreaterThan(Mockito.any(), Mockito.any());
    }

    @Test
    public void getAlbumsByAfterCursor_shouldCallfindAllByIdGreaterThan_whenAfterCursorIsEmptyValue() {
        Page page = new PageImpl(new ArrayList<>());
        Mockito.when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        service.getAlbumsByAfterCursor(null, 2);

        Mockito.verify(repository, Mockito.times(1)).findAll((Pageable) Mockito.any());
    }
}