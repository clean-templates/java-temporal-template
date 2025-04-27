package com.rolandsall.config;

import com.rolandsall.config.tracing.OpenTracingWorkerInterceptorProvider;
import io.temporal.spring.boot.TemporalOptionsCustomizer;
import io.temporal.worker.WorkerFactoryOptions;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkerFactoryOptionsCustomizer implements TemporalOptionsCustomizer<WorkerFactoryOptions.Builder> {

    private final OpenTracingWorkerInterceptorProvider openTracingWorkerInterceptorProvider;

    @NonNull
    @Override
    public WorkerFactoryOptions.Builder customize(@NonNull WorkerFactoryOptions.Builder optionsBuilder) {
        return optionsBuilder
                .setWorkerInterceptors(openTracingWorkerInterceptorProvider.get(null));
    }
}
