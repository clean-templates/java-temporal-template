package com.rolandsall.config.tracing;

import io.opentracing.Tracer;
import io.temporal.opentracing.OpenTracingClientInterceptor;
import io.temporal.opentracing.OpenTracingOptions;
import io.temporal.opentracing.OpenTracingSpanContextCodec;


public class ConcreteOpenTracingClientInterceptorProvider implements OpenTracingClientInterceptorProvider {
    @Override
    public OpenTracingClientInterceptor get(Tracer tracer) {
        return new OpenTracingClientInterceptor(getOptions(tracer));
    }

    private static OpenTracingOptions getOptions(Tracer tracer) {
        return OpenTracingOptions.newBuilder()
                .setSpanContextCodec(OpenTracingSpanContextCodec.TEXT_MAP_CODEC)
                .setTracer(tracer)
                .build();
    }
}
