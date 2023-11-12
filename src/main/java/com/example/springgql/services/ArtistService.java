package com.example.springgql.services;

import com.example.springgql.exception.Constants;
import com.example.springgql.exception.DataNotCreatedException;
import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.ArtistInput;
import com.example.springgql.models.graphqlInput.DeletePayload;
import com.example.springgql.repositories.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService {
    final ArtistRepository repository;

    public Artist saveArtistInput(ArtistInput artistInput) {
        artistInput.setName(artistInput.getName().toLowerCase());
        Artist findItemByName = repository.findItemByName(artistInput.getName());
        Artist entity = Artist.builder()
                .name(artistInput.getName().toLowerCase())
                .displayName(artistInput.getDisplayName())
                .build();

        if (null != findItemByName) {
            throw new DataNotCreatedException(Artist.class, Constants.RECORD_ALREADY_EXIST);
        }
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
            throw new DataNotFoundException(Artist.class);
        }
        return byId.get();
    }

    public Artist updateArtistById(String id, ArtistInput artistInput) {
        Optional<Artist> byId = repository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotFoundException(Artist.class);
        }
        Artist artist = byId.get();
        artist.setName(artistInput.getName());
        return artist;
    }

    public DeletePayload deleteById(String id) {
        if (!repository.findById(id).isPresent()) {
            throw new DataNotFoundException(Artist.class);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            return DeletePayload.builder()
                    .message("")
                    .success(false)
                    .build();
        }

        return DeletePayload.builder()
                .message("")
                .success(true)
                .build();
    }

//    public void updateAlbumOnArtist(Album save, Artist artistByName) {
//        List<Album> albumsList = artistByName.getAlbumsList();
//        albumsList.add(save);
//        repository.save(artistByName);
//    }
}
