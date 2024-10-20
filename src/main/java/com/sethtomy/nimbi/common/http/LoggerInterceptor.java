package com.sethtomy.nimbi.common.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class LoggerInterceptor implements ClientHttpRequestInterceptor {
    private final Logger log = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public ClientHttpResponse intercept(
      HttpRequest request,
      byte[] body,
      ClientHttpRequestExecution execution
    ) throws IOException {
        log.info("Sending a {} request to {}...", request.getMethod(), request.getURI());
        Instant start = Instant.now();
        ClientHttpResponse response = execution.execute(request, body);
        Duration duration = Duration.between(start, Instant.now());
        long seconds = duration.getSeconds();
        long nanos = duration.minusSeconds(seconds).getNano() % 1000_000_000L;
        log.info("[{}.{}s] Successfully sent {} response to {}.", seconds, nanos, request.getMethod(), request.getURI());
        return response;
    }
}
