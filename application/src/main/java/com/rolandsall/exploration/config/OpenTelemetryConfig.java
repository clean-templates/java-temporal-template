package com.rolandsall.exploration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class OpenTelemetryConfig {


    private final OpenTelemetryConfigProperties openTelemetryConfigProperties;

    @Value("${spring.application.name}")
    private String applicationName;



}
