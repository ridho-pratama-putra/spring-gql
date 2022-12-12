package com.example.springgql.models.graphqlInput;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
@Data
public class AlbumInput {
    String title;
    String releaseDate;
    String totalSong;
    String duration;
    ArtistInput artist;
}
