package com.example.springgql.services;

import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.models.Album;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.AlbumInput;
import com.example.springgql.repositories.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    final AlbumRepository repository;
    final ArtistService artistService;

    public Album saveAlbumOnArtist(AlbumInput albumInput) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Artist artistByName = artistService.getArtistByName(albumInput.getArtist().getName());
        if (artistByName == null) {
            throw new DataNotFoundException("Data not found");
        }
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
        Iterable<Album> all = repository.findAllByArtistId(id);
        List<Album> result = new ArrayList<>();
        all.forEach(result::add);
        return result;
    }

    public List<Album> getAllAlbums() {
        return repository.findAll();
    }
}
