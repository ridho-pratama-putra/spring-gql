package com.example.springgql.models;

import com.example.springgql.enums.CategoryEnum;
import com.example.springgql.enums.ReleaseType;
import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@QueryEntity
@Document
@Builder
@Data
public class Release {
    @Id
    String id;
    String title;
    List<CategoryEnum> category;

    ReleaseType releaseType;

    @CreatedBy
    private String createdByUser;

    @CreatedDate
    private LocalDateTime creationDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedUserId;

    LocalDateTime releaseDate;
    List<Song> songList;
    String duration;
    @DocumentReference(lazy = true)
    List<Artist> artist;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
    }
}
