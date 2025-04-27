package com.rolandsall.exploration.config;

import com.rolandsall.exploration.config.tracing.OpenTracingClientInterceptorProvider;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.spring.boot.TemporalOptionsCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkflowClientOptionsCustomizer implements TemporalOptionsCustomizer<WorkflowClientOptions.Builder> {

    private final OpenTracingClientInterceptorProvider openTracingClientInterceptorProvider;

    @Override
    public WorkflowClientOptions.Builder customize(WorkflowClientOptions.Builder optionsBuilder) {
        return optionsBuilder
                .setInterceptors(openTracingClientInterceptorProvider.get(null));

    }
}