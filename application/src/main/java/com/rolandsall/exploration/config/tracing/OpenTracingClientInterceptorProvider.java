package com.rolandsall.exploration.config.tracing;

import io.opentracing.Tracer;
import io.temporal.opentracing.OpenTracingClientInterceptor;

public interface OpenTracingClientInterceptorProvider {
    OpenTracingClientInterceptor get(Tracer tracer);
}
