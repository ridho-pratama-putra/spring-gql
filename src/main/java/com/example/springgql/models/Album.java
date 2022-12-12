package com.example.springgql.models;

import com.example.springgql.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
@Data
public class Album {
    @Id
    String id;
    String title;
    CategoryEnum categoryEnum;
    Date addedDate;
    String releaseDate;
    List<Song> songList;
    String duration;
    @DocumentReference(lazy = true)
    Artist artist;
}
