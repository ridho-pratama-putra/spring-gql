package com.example.springgql.exception;

public class DataNotCreatedException extends RuntimeException {
    public DataNotCreatedException(Class<?> modelName, String message) {
        super("Failed to create ".concat(modelName.getSimpleName().toString()).concat(" record. ".concat(message)));
    }

    public DataNotCreatedException(Class<?> modelName) {
        super("Failed to create ".concat(modelName.getSimpleName().toString()).concat(" record."));
    }
}
