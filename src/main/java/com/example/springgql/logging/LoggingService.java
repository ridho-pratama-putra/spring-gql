package com.example.springgql.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingService {

    Logger logger = LoggerFactory.getLogger(LoggingService.class);

    public void write(LoggingModel loggingModel) {
        String ob;
        try {
            ob = new ObjectMapper().writeValueAsString(loggingModel);
            logger.info(ob);
        } catch (JsonProcessingException e) {
            logger.error(e.getOriginalMessage());
        }
    }

}
