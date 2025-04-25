package com.poc.exploration.testing;

import io.temporal.spring.boot.autoconfigure.properties.WorkerProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.temporal")
public class TemporalWorkerConfiguration {

    private List<WorkerProperties> workers;

    public Optional<List<WorkerProperties>> getWorkers() {
        return Optional.ofNullable(workers);
    }
}
