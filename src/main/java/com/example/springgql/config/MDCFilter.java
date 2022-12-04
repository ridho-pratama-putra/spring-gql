package com.example.springgql.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Order(1)
@Component
public class MDCFilter implements Filter {

    private final Logger LOGGER = LoggerFactory.getLogger(MDCFilter.class);
    private final String X_REQUEST_ID = "X-Request-ID";

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        try {
            addXRequestId(req);
            LOGGER.info("path: {}, method: {}, query {}",
                    req.getRequestURI(), req.getMethod(), req.getQueryString());
            res.setHeader(X_REQUEST_ID, MDC.get(X_REQUEST_ID));
            chain.doFilter(request, response);
        } finally {
            LOGGER.info("statusCode {}, path: {}, method: {}, query {}",
                    res.getStatus(), req.getRequestURI(), req.getMethod(), req.getQueryString());
            MDC.clear();
        }
    }

    private void addXRequestId(HttpServletRequest request) {
        String xRequestId = request.getHeader(X_REQUEST_ID);
        if (xRequestId == null) {
            MDC.put(X_REQUEST_ID, UUID.randomUUID().toString());
        } else {
            MDC.put(X_REQUEST_ID, xRequestId);
        }
    }
}
