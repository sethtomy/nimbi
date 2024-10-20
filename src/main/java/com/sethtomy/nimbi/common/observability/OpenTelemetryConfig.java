package com.sethtomy.nimbi.common.observability;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
public class OpenTelemetryConfig {
    @Value("${otel.host}")
    private String host;

    @Bean
    public OtlpGrpcSpanExporter otlpGrpcSpanExporter() {
        return OtlpGrpcSpanExporter.builder().setEndpoint(host).build();
    }
}
