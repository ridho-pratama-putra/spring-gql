package com.example.springgql.services;

import com.example.springgql.exception.Constants;
import com.example.springgql.exception.DataNotCreatedException;
import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.models.Artist;
import com.example.springgql.models.QRelease;
import com.example.springgql.models.Release;
import com.example.springgql.models.graphqlInput.DeletePayload;
import com.example.springgql.models.graphqlInput.ReleaseInput;
import com.example.springgql.repositories.ReleaseRepository;
import com.example.springgql.utils.CursorUtil;
import com.querydsl.core.types.Predicate;

import graphql.relay.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReleaseService {

    private final ReleaseRepository repository;
    private final ArtistService artistService;
    private final MongoTemplate template;

    Logger logger = LoggerFactory.getLogger(ReleaseService.class);

    private final RestTemplate restTemplate;

    public Release saveReleaseOnArtist(ReleaseInput releaseInput) {
        if (releaseInput.getArtist() == null) {
            throw new DataNotCreatedException(Release.class, Constants.MISSING_REQUIRED_FIELD);
        }
        // SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Artist artistByName = artistService.getArtistById(releaseInput.getArtist().getId());
        if (artistByName == null) {
            throw new DataNotFoundException(Artist.class);
        }
        ObjectId artistId = new ObjectId(releaseInput.getArtist().getId());
        Release findByTitleAndArtistId = repository.findByTitleIgnoreCaseAndArtistId(releaseInput.getTitle(), artistId);
        if (findByTitleAndArtistId != null) {
            throw new DataNotCreatedException(Release.class, Constants.RECORD_ALREADY_EXIST);
        }
        Release entity;
        entity = Release.builder()
                .title(releaseInput.getTitle())
                .releaseDate(LocalDateTime.parse(releaseInput.getReleaseDate()))
                .category(releaseInput.getCategory())
                .releaseType(releaseInput.getReleaseType())
                .artist(List.of(artistByName))
                .build();

        HttpEntity<Release> request = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Request-ID", MDC.get("X-Request-ID"));
        request = new HttpEntity<>(Release.builder()
                .artist(List.of(artistByName))
                .build(), httpHeaders);

        try {
            restTemplate.postForEntity("http://localhost:8082/album", request, Release.class);
        } catch (RestClientException restClientException) {
            logger.info("::: " + restClientException.getMessage());
            // throw new DataNotCreatedException(Release.class, Constants.INTERNAL_SERVER_ERROR);
        }
        Release save = repository.save(entity);
        return save;
    }

    public DefaultConnection<Release> getReleasesByArtistId(String id, int first, String after) {
        List<Release> all = getReleasesOnArtistByAfterCursor(id, after, first);
        List<Edge<Release>> defaultEdges = all
                .stream()
                .map(album -> new DefaultEdge<Release>(album, CursorUtil.convertCursorFromId(album.getId())))
                .limit(first)
                .collect(Collectors.toList());

        ConnectionCursor startCursor = CursorUtil.getConnectionCursor(defaultEdges, 0);
        ConnectionCursor endCursor = CursorUtil.getConnectionCursor(defaultEdges, defaultEdges.size() - 1);

        DefaultPageInfo defaultPageInfo = new DefaultPageInfo(
                startCursor,
                endCursor,
                after != null,
                defaultEdges.size() >= first
        );

        return new DefaultConnection<>(defaultEdges, defaultPageInfo);
    }

    public DefaultConnection<Release> getAllReleases(String after, int limit) {
        List<Release> all = getReleasesByAfterCursor(after, limit);
        List<Edge<Release>> defaultEdges = all
                .stream()
                .map(album -> {
                    return new DefaultEdge<Release>(album, CursorUtil.convertCursorFromId(album.getId()));
                })
                .limit(limit)
                .collect(Collectors.toList());

        ConnectionCursor startCursor = CursorUtil.getConnectionCursor(defaultEdges, 0);
        ConnectionCursor endCursor = CursorUtil.getConnectionCursor(defaultEdges, defaultEdges.size() - 1);

        DefaultPageInfo defaultPageInfo = new DefaultPageInfo(
                startCursor,
                endCursor,
                after != null,
                defaultEdges.size() >= limit
        );

        return new DefaultConnection<>(defaultEdges, defaultPageInfo);
    }

    private List<Release> getReleasesByAfterCursor(String afterCursor, int limit) {
        if (afterCursor == null || afterCursor.isEmpty()) {
            return repository.findAll(Pageable.ofSize(limit)).toList();
        }
        String afterID = CursorUtil.convertIdFromCursor(afterCursor);
        ObjectId objectId = new ObjectId(afterID);
//        Query query = new Query();
//        query.addCriteria(Criteria.where("_id").gt(objectId));
//        return template.find(query, Album.class);
        return repository.findAllByIdGreaterThan(objectId, Pageable.ofSize(limit));
    }

    private List<Release> getReleasesOnArtistByAfterCursor(String id, String afterCursor, int limit) {
        ObjectId artistId = new ObjectId(id);
        
        if (afterCursor == null || afterCursor.isEmpty()) {
            return repository.findAllByArtistId(artistId, Pageable.ofSize(limit));
        }
        
        String afterID = CursorUtil.convertIdFromCursor(afterCursor);
        ObjectId objectId = new ObjectId(afterID);
//        Query query = new Query();
//        query.addCriteria(Criteria.where("_id").gt(objectId));
//        return template.find(query, Album.class);
        return repository.findAllByIdGreaterThanAndArtistId(objectId, artistId, Pageable.ofSize(limit));
    }

    public Map<Artist, List<Release>> getReleasesByArtistIds(List<String> artistIds) {
        
        // // 1. Konversi String ID menjadi ObjectId (Hanya perlu di sini)
        // List<ObjectId> objectIds = artistIds.stream()
        //     .map(ObjectId::new)
        //     .collect(Collectors.toList());

        // // 2. Akses Database (Single Call)
        // List<Release> allReleases = repository.findAllByArtistIdIn(objectIds);


        // all previos code above IGNORED due to QueryDSL implementation below
        // more clean and some says it was efficient for large data set
        Predicate predicate = QRelease.release.artist.any().id.in(artistIds);
        Iterable<Release> allReleases = repository.findAll(predicate);

        // 3. EFFICIENT GROUPING (The Core Logic)
        // Mengelompokkan List<Release> yang besar menjadi Map<Artist ID, List<Release>>
        Map<Artist, List<Release>> releasesByArtistId = new HashMap<>();

        for (Release release : allReleases) {
            logger.info("Release ID: [{}]",  release.getArtist().size());
            for (Artist artist : release.getArtist()) {
                releasesByArtistId
                    .computeIfAbsent(artist, k -> new ArrayList<>())
                    .add(release);
            }
        }

        // 4. Pengembalian: Langsung mengembalikan Map<String ID, List<Release>>.
        // Spring GraphQL/DataLoader yang akan mencocokkan hasilnya ke setiap Artist.
        return releasesByArtistId;
    }

    public List<Release> getAllReleasesRest() {
        return getReleasesByAfterCursor(null, 10);

    }

    public Release updateReleaseById(String id, ReleaseInput releaseInput) {

        Optional<Release> byId = repository.findById(id);
        if (!byId.isPresent()) {
            throw new DataNotFoundException(Release.class);
        }

        Release currentRelease = byId.get();
        currentRelease.setTitle(releaseInput.getTitle());

        if (releaseInput.getCategory() != currentRelease.getCategory()) {
            currentRelease.setCategory(releaseInput.getCategory());
        }

        Release updatedValue = repository.save(currentRelease);
        return updatedValue;
    }

    public DeletePayload deleteById(String id) {
        if (!repository.findById(id).isPresent()) {
            throw new DataNotFoundException(Release.class);
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
}
