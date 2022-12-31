package com.example.springgql.services;

import com.example.springgql.exception.DataNotFoundException;
import com.example.springgql.models.Album;
import com.example.springgql.models.Artist;
import com.example.springgql.models.graphqlInput.AlbumInput;
import com.example.springgql.repositories.AlbumRepository;
import com.example.springgql.utils.CursorUtil;
import com.google.common.collect.Iterables;
import graphql.relay.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {

    final AlbumRepository repository;
    final ArtistService artistService;
    final MongoTemplate template;

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
        if (Iterables.isEmpty(all)) {
            throw new DataNotFoundException("Data not found");
        }
        List<Album> result = new ArrayList<>();
        all.forEach(result::add);
        return result;
    }

    public DefaultConnection<Album> getAllAlbums(String after, int first) {
        List<Album> all = getAlbumsByAfterCursor(after, first);
        List<Edge<Album>> defaultEdges = all
                .stream()
                .map(album -> new DefaultEdge<Album>(album, CursorUtil.convertCursorFromId(album.getId())))
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

    public List<Album> getAlbumsByAfterCursor(String afterCursor, int limit) {
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

    public Map<Artist, List<Album>> getAlbumsByArtistIds(List<Artist> artists) {
        List<ObjectId> collect = artists.stream().map(artist -> new ObjectId(artist.getId())).collect(Collectors.toList());
        List<Album> allByArtistIdIn = repository.findAllByArtistIdIn(collect);
        return artists.stream()
                .collect(Collectors.toMap(Function.identity(), artist -> allByArtistIdIn.stream().filter(album -> album.getArtist().getId().equals(artist.getId()))
                        .collect(Collectors.toList())));
    }
}
