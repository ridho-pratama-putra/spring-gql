package com.example.springgql.exception;

import org.springframework.graphql.execution.ErrorType;

/*
 * this class using cause for graphql error classification
 * cause passed as strings and then @CustomExceptionResolver will translate back to ErrorType
 * message value depend on modelname passed as Object
 */
public class DataNotCreatedException extends RuntimeException {
    public DataNotCreatedException(Object modelName) {
        super("Failed to create ".concat(modelName.getClass().getSimpleName().toString()).concat(" record"), new Throwable(ErrorType.BAD_REQUEST.toString()));        
    }
}
