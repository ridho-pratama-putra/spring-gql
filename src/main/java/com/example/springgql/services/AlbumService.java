package com.example.springgql.services;

import com.example.springgql.models.Album;
import com.example.springgql.models.Artist;
import com.example.springgql.models.QAlbum;
import com.example.springgql.models.graphqlInput.AlbumInput;
import com.example.springgql.repositories.AlbumRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        Iterable<Album> allByArtistId = repository.findAllByArtistId(id);
        List<Album> list = new ArrayList<>();

        // Add each element of iterator to the List
        allByArtistId.forEach(list::add);
        return list;
    }

    public List<Album> getAllAlbums() {
        QAlbum qUser = new QAlbum("album");
        Predicate predicate = qUser.title.isNotNull();
        List<Album> users = (List<Album>) repository.findAll(predicate);
        return users;
    }
}
