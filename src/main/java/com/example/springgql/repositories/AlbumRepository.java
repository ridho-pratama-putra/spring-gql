package com.example.springgql.repositories;

import com.example.springgql.models.Album;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.graphql.data.GraphQlRepository;

import java.util.List;

@GraphQlRepository
public interface AlbumRepository extends MongoRepository<Album, String>, QuerydslPredicateExecutor<Album> {

    @Query(value = "{'artist._id': '?0'}")
    List<Album> findAllByArtistId(String id);

}
