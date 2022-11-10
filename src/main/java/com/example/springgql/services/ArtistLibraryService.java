package com.example.springgql.services;

import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistLibraryService {

    @Autowired
    ArtistRepository repository;

    public Artist saveArtist(ArtistInput artistInput) {
        Artist entity = Artist.builder()
                .name(artistInput.getName())
                .albums(null)
                .build();
        return repository.save(entity);
    }
}
