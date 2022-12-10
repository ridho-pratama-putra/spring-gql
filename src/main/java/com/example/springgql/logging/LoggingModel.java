package com.example.springgql.logging;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class LoggingModel {
    String event;
    String traceId;
    String spanId;
    Long startTime;
    Long finishTime;
    Long elapsedTime;
    Map<String,String> additionalProperties;
    int httpStatus;
    String username;
    String url;
    String clientType;
    String requestHeader;
    String requestBody;
    String httpMethod;
    String errorMessage;
}
