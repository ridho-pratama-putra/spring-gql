package com.example.springgql.config;

import com.example.springgql.logging.LoggingModel;
import com.example.springgql.logging.LoggingService;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GraphQLRequestLoggingInstrumentation extends SimpleInstrumentation {
    private final Clock clock;
    private final LoggingService loggingService;
    private final HttpServletRequest request;
    private final Tracer tracer;

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        Instant start = Instant.now();
        String traceId = tracer.nextSpan().context().traceId();
        String spanId = tracer.nextSpan().context().spanId();
        return SimpleInstrumentationContext.whenCompleted(((executionResult, throwable) -> {
            Duration duration = Duration.between(start, Instant.now(clock));
            List<GraphQLError> errors = executionResult.getErrors();
            if (errors.isEmpty()) {
                loggingService.write(LoggingModel.builder()
                        .event("GraphQL")
                        .traceId(traceId)
                        .spanId(spanId)
                        .clientType(request.getHeader("user-agent"))
                        .requestBody(parameters.getQuery())
                        .startTime(start.toEpochMilli())
                        .elapsedTime(duration.toMillis())
                        .httpMethod("POST")
                        .httpStatus(HttpStatus.OK.value())
                        .url("/graphql")
                        .build());
            } else {
                loggingService.write(LoggingModel.builder()
                        .event("GraphQL")
                        .traceId(traceId)
                        .spanId(spanId)
                        .clientType(request.getHeader("user-agent"))
                        .requestBody(parameters.getQuery())
                        .startTime(start.toEpochMilli())
                        .elapsedTime(duration.toMillis())
                        .httpMethod("POST")
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errorMessage(errors.get(0).getMessage())
                        .url("/graphql")
                        .build());
            }
        }));
    }
}
