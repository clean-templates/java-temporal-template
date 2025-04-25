package com.poc.exploration.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "open-telemetry")
public class OpenTelemetryConfigProperties {
    private String endpoint;
}
