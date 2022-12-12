package com.example.springgql.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
@Data
public class Song {
    String id;
    String title;
    String duration;
    String totalPlayed;
    @DBRef
    Album album;
}
