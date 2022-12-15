package com.example.springgql.models;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.OneToMany;
import java.util.List;

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
    @OneToMany
    List<Album> albumsList;
}
