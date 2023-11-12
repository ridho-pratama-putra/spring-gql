package com.example.springgql.exception;

public class DataNotUpdatedException extends RuntimeException {
    public DataNotUpdatedException(Class<?> modelName, String message) {
        super("Failed to delete ".concat(modelName.getSimpleName().toString()).concat(" record. ".concat(message)));
    }

    public DataNotUpdatedException(Class<?> modelName) {
        super("Failed to update ".concat(modelName.getSimpleName().toString()).concat(" record."));
    }
}
