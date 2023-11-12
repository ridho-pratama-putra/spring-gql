package com.example.springgql.models.graphqlInput;

import com.example.springgql.enums.CategoryEnum;
import com.example.springgql.enums.ReleaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
@Data
public class ReleaseInput {
    String title;
    String releaseDate;
    String totalSong;
    String duration;

    List<CategoryEnum> category;
    ReleaseType releaseType;
    ArtistInput artist;
}
