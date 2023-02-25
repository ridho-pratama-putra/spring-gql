package com.example.springgql.services;

import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.models.Release;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.ReleaseInput;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.repositories.ReleaseRepository;
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
class ReleaseServiceTest {

    @MockBean
    ReleaseRepository repository;

    @MockBean
    ArtistService artistService;

    @Autowired
    ReleaseService service;

    @Test
    public void saveAlbumOnArtist_shouldReturnEntity_whenSuccessSaveArtistNameExist() {
        Artist artist = Artist.builder()
                .name("endank")
                .build();
        Release release = Release.builder()
                .title("juara")
                .build();
        Mockito.when(artistService.getArtistByName(Mockito.any())).thenReturn(artist);
        Mockito.when(repository.save(Mockito.any())).thenReturn(release);
        ReleaseInput juara = ReleaseInput.builder()
                .title("juara")
                .artist(ArtistInput.builder().name("endank").build())
                .releaseDate("2012-12-13T00:00:00")
                .build();

        Release actualResult = service.saveReleaseOnArtist(juara);

        Assertions.assertEquals("juara", actualResult.getTitle());
    }

    @Test
    public void saveAlbumOnArtist_shouldReturnExceptionDataNotFoundException_whenArtistNotFound() {
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            ReleaseInput juara = ReleaseInput.builder()
                    .artist(ArtistInput.builder().name("endank").build())
                    .build();
            Mockito.when(artistService.getArtistByName(Mockito.any())).thenReturn(null);
            service.saveReleaseOnArtist(juara);
        });
    }

    @Test
    public void getAlbumsByArtistId_shouldReturnEntity_whenDataIsExist() {
        String fakeID = "63c911b6a272812098224d7c";
        ArrayList<Release> releases = new ArrayList<>();
        releases.add(Release.builder().id(fakeID).build());
        Mockito.when(repository.findAllByArtistId(Mockito.any(), Mockito.any())).thenReturn(releases);

        DefaultConnection<Release> albumsByArtistId = service.getReleasesByArtistId(fakeID, 1, "");
        Assertions.assertNotNull(albumsByArtistId);
    }

    @Test
    public void getAlbumsByAfterCursor_shouldCallfindAllByIdGreaterThan_whenAfterCursorIsNotEmptyValue() {
        service.getReleasesByAfterCursor("NjNhOWIyYzY5MzRhNzk3ZGZlMzMyMmY5", 2);

        Mockito.verify(repository, Mockito.times(1)).findAllByIdGreaterThan(Mockito.any(), Mockito.any());
    }

    @Test
    public void getAlbumsByAfterCursor_shouldCallfindAllByIdGreaterThan_whenAfterCursorIsEmptyValue() {
        Page page = new PageImpl(new ArrayList<>());
        Mockito.when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        service.getReleasesByAfterCursor(null, 2);

        Mockito.verify(repository, Mockito.times(1)).findAll((Pageable) Mockito.any());
    }
}