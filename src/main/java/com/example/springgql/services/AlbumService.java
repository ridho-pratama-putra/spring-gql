package com.example.springgql.services;

import com.example.springgql.models.Album;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.AlbumInput;
import com.example.springgql.repositories.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    final AlbumRepository repository;
    final ArtistService artistService;

    public Album saveAlbumOnArtist(AlbumInput albumInput) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Artist artistByName = artistService.getArtistByName(albumInput.getArtist().getName());
        Album entity;
        try {
            entity = Album.builder()
                    .title(albumInput.getTitle())
                    .addedDate(date.getCalendar().getTime())
                    .releaseDate(date.parse(albumInput.getReleaseDate()).toString())
                    .artist(artistByName)
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return repository.save(entity);
    }

    public List<Album> getAlbumsByArtistId(String id) {
        return repository.findAllByArtistId(id);
    }
}
