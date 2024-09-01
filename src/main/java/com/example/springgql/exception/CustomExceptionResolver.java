package com.example.springgql.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {
    private final Tracer tracer;

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        Map<String, Object> extensionCustom = new HashMap<>();
        extensionCustom.put("traceId", tracer.nextSpan().context().traceId());
        if (InternalServerError.class.equals(ex.getClass())) {
            System.out.println(ex);
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(extensionCustom)
                .build();
        }
        
        if (ConnectException.class.equals(ex.getClass())) { // forget for which kind of case
            return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(extensionCustom)
                .build();
        }
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(extensionCustom)
                .build();
    }
}