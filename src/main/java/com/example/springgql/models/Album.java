package com.example.springgql.models;

import com.example.springgql.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Document
@Data
public class Album {
    @Id
    String id;
    String title;
    CategoryEnum categoryEnum;
    Date date;
}
