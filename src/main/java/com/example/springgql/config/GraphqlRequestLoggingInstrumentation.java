package com.example.springgql.config;

import com.example.springgql.logging.LoggingModel;
import com.example.springgql.logging.LoggingService;
import graphql.ExecutionResult;
import graphql.execution.ExecutionId;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class GraphqlRequestLoggingInstrumentation extends SimpleInstrumentation {
    private final Clock clock;
    Logger log = LoggerFactory.getLogger(GraphqlRequestLoggingInstrumentation.class);

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        Instant start = Instant.now();
        ExecutionId executionId = parameters.getExecutionInput().getExecutionId();
        log.info("[INCOMING] execution id: {} query: {} parameters: {}", executionId, parameters.getQuery(), parameters.getVariables());
        return SimpleInstrumentationContext.whenCompleted(((executionResult, throwable) -> {
            Duration duration = Duration.between(start, Instant.now(clock));
            if (throwable == null) {
                log.info("[SUCCESS] execution id: {} duration: {}ms", executionId, duration.toMillis());
            } else {
                log.warn("[FAILED] execution id {}", executionId, duration, throwable);
            }
        }));
    }
}
