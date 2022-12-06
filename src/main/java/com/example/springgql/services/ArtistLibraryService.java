package com.example.springgql.services;

import com.example.springgql.enums.CategoryEnum;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistLibraryService {
    final ArtistRepository repository;
    final RestTemplate restTemplate;

    public Artist saveArtist(ArtistInput artistInput) {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        HttpEntity<Album> request = null;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("X-Request-ID", MDC.get("X-Request-ID"));
            request = new HttpEntity<>(new Album("krupuk", "rambak", CategoryEnum.ROCK, date.parse("19/08/1945")), httpHeaders);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ResponseEntity<Album> albumResponseEntity = restTemplate.postForEntity("http://localhost:8082/album", request, Album.class);
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
