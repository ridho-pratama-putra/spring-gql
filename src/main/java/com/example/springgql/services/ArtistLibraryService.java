package com.example.springgql.services;

import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.repositories.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistLibraryService {

    ArtistRepository repository;

    ArtistLibraryService(ArtistRepository repository) {
        this.repository = repository;
    }

    public Artist saveArtist(ArtistInput artistInput) {
        Artist entity = Artist.builder()
                .name(artistInput.getName())
                .albums(null)
                .build();
        return repository.save(entity);
    }

    public List<Artist> getAllArtist() {
        return repository.findAll();
    }
}