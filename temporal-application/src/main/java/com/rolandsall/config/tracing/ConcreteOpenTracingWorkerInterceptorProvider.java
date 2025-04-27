package com.rolandsall.config.tracing;

import io.opentracing.Tracer;
import io.temporal.opentracing.OpenTracingOptions;
import io.temporal.opentracing.OpenTracingSpanContextCodec;
import io.temporal.opentracing.OpenTracingWorkerInterceptor;

public class ConcreteOpenTracingWorkerInterceptorProvider implements OpenTracingWorkerInterceptorProvider {
    @Override
    public OpenTracingWorkerInterceptor get(Tracer tracer) {
        return new OpenTracingWorkerInterceptor(getOpenTracingOptions(tracer));
    }

    private static OpenTracingOptions getOpenTracingOptions(Tracer tracer) {
        return OpenTracingOptions.newBuilder()
                .setSpanContextCodec(OpenTracingSpanContextCodec.TEXT_MAP_CODEC)
                .setTracer(tracer)
                .build();
    }

}
