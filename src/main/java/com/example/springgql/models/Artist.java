package com.example.springgql.models;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@QueryEntity
@Document
@Builder
@Data
public class Artist {
    @Id
    String id;
    String name;

//    @DocumentReference(lazy=true)
//    private List<Album> albumsList;
}
