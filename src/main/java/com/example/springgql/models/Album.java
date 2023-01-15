package com.example.springgql.models;

import com.example.springgql.enums.CategoryEnum;
import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@QueryEntity
@Document
@Builder
@Data
public class Album {
    @Id
    String id;
    String title;
    CategoryEnum categoryEnum;

    @CreatedBy
    private String createdByUser;

    @CreatedDate
    private LocalDateTime creationDate = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));

    @LastModifiedDate
    private Date lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedUserId;

    Date releaseDate;
    List<Song> songList;
    String duration;
    @DocumentReference(lazy = true)
    Artist artist;
}
