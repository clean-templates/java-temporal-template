package com.rolandsall.exploration.config.tracing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalTracingConfig {

    @Bean
    public OpenTracingClientInterceptorProvider openTracingClientInterceptorProvider() {
        return new ConcreteOpenTracingClientInterceptorProvider();
    }

    @Bean
    public OpenTracingWorkerInterceptorProvider openTracingWorkerInterceptorProvider() {
        return new ConcreteOpenTracingWorkerInterceptorProvider();
    }
}
