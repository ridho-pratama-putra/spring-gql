package com.example.springgql.services;

import com.example.springgql.exception.DataNotCreatedException;
import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.models.Release;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.repositories.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService {
    final ArtistRepository repository;

    public Artist saveArtistInput(ArtistInput artistInput) {
        Artist entity = Artist.builder()
                .name(artistInput.getName())
                .build();
        return repository.save(entity);
    }

    public List<Artist> getAllArtist() {
        return repository.findAll();
    }

    public Artist getArtistByName(String name) {
        return repository.findItemByName(name);
    }

    public Artist getArtistById(String id) {
        Optional<Artist> byId = repository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotFoundException("not foung");
        }
        return byId.get();
    }

//    public void updateAlbumOnArtist(Album save, Artist artistByName) {
//        List<Album> albumsList = artistByName.getAlbumsList();
//        albumsList.add(save);
//        repository.save(artistByName);
//    }
}
