package com.example.springgql.exception;

import com.example.springgql.models.Artist;

public class DataNotCreatedException extends RuntimeException {
    public DataNotCreatedException(Class<Artist> artistClass) {
        super("Failed to create ".concat(artistClass.getClass().getName()).concat(" record"));
    }
}
