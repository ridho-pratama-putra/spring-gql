package com.example.springgql.repositories;

import com.example.springgql.models.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends MongoRepository<Artist, String> {
    @Query("{name:'?0'}")
    Artist findItemByName(String name);

    public long count();
}
