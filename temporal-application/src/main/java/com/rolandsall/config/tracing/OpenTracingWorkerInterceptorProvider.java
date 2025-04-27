package com.rolandsall.config.tracing;

import io.opentracing.Tracer;
import io.temporal.opentracing.OpenTracingWorkerInterceptor;

public interface OpenTracingWorkerInterceptorProvider {
    OpenTracingWorkerInterceptor get(Tracer tracer);
}
