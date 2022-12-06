package com.example.springgql.config;

import com.example.springgql.logging.LoggingModel;
import com.example.springgql.logging.LoggingService;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Order(2)
public class GraphqlRequestLoggingInstrumentation extends SimpleInstrumentation {
    private final Clock clock;
    private final LoggingService loggingService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        Instant start = Instant.now();
        String executionId = response.getHeader("X-Request-ID"); // required due to will be saved on log as eventId
        return SimpleInstrumentationContext.whenCompleted(((executionResult, throwable) -> {
            Duration duration = Duration.between(start, Instant.now(clock));
            List<GraphQLError> errors = executionResult.getErrors();
            if (errors.isEmpty()) {
                Map<Object, Object> data = executionResult.getData();
                data.put("executionId", executionId);
                loggingService.write(LoggingModel.builder()
                        .event("GraphQL")
                        .eventId(executionId)
                        .spanId(response.getHeader("X-Span-ID"))
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
                        .eventId(executionId)
                        .spanId(response.getHeader("X-Span-ID"))
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
