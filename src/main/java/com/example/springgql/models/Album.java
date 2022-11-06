package com.example.springgql.models;

import com.example.springgql.enums.Category;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class Album {
    String title;
    Category category;
    Date date;
}
