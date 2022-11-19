package com.example.springgql.logging;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class LoggingModel {
    String event;
    Long startTime;
    Map<String,Object> additionalProperties;
    int httpStatus;
    String username;
    String path;
    String url;
    String requestHeader;
    String requestBody;
    String httpMethod;
}
