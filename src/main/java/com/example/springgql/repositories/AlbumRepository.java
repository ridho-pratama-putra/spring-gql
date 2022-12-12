package com.example.springgql.repositories;

import com.example.springgql.models.Album;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends MongoRepository<Album, String> {

    @Query(value = "{'artist._id': '?0'}")
    List<Album> findAllByArtistId(String id);
}
