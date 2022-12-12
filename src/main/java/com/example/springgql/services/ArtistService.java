package com.example.springgql.services;

import com.example.springgql.exception.DataNotCreatedException;
import com.example.springgql.models.Album;
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

@Service
@RequiredArgsConstructor
public class ArtistService {
    final ArtistRepository repository;
    final RestTemplate restTemplate;

    public Artist saveArtistInput(ArtistInput artistInput) {
        HttpEntity<Album> request = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Request-ID", MDC.get("X-Request-ID"));
        request = new HttpEntity<>(new Album(), httpHeaders);
        ResponseEntity<Album> newestReleaseRecommendationResult = restTemplate.postForEntity("http://localhost:8082/album", request, Album.class);
        if(newestReleaseRecommendationResult.getStatusCode().is2xxSuccessful()) {
            Artist entity = Artist.builder()
                    .name(artistInput.getName())
                    .build();
            return repository.save(entity);
        }
        throw new DataNotCreatedException(Artist.class);
    }

    public List<Artist> getAllArtist() {
        return repository.findAll();
    }

    public Artist getArtistByName(String name) {
        return repository.findItemByName(name);
    }

    public Artist saveArtist(Artist artistByName) {
        return repository.save(artistByName);
    }
}
