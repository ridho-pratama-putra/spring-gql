package com.example.springgql.exception;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(Class<?> class1) {
        super(class1.getSimpleName().toString().concat(" not found."));
    }
}