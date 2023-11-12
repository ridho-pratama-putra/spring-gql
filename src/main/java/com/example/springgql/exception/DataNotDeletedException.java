package com.example.springgql.exception;

public class DataNotDeletedException extends RuntimeException {
    public DataNotDeletedException(Class<?> modelName, String message) {
        super("Failed to delete ".concat(modelName.getSimpleName().toString()).concat(" record. ".concat(message)));
    }

    public DataNotDeletedException(Class<?> modelName) {
        super("Failed to delete ".concat(modelName.getSimpleName().toString()).concat(" record."));
    }
}
