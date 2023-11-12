package com.example.springgql.repositories;

import com.example.springgql.models.Release;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.graphql.data.GraphQlRepository;

import java.util.List;

@GraphQlRepository
public interface ReleaseRepository extends MongoRepository<Release, String>, QuerydslPredicateExecutor<Release> {

    @Query(value = "{'artist._id': '?0'}")
    List<Release> findAllByArtistId(String id);

    List<Release> findAllByIdGreaterThan(ObjectId lastId, Pageable pageable);

    List<Release> findAllByIdGreaterThanAndArtistId(ObjectId lastId, ObjectId artistId, Pageable pageable);

    List<Release> findAllByArtistId(ObjectId artistId, Pageable pageable);

    List<Release> findAllByArtistIdIn(List<ObjectId> artists);

    Release findByTitleIgnoreCaseAndArtistId(String title, ObjectId artistId);
}
