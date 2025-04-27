package com.rolandsall.testing;

import com.google.common.annotations.VisibleForTesting;
import io.temporal.api.enums.v1.EventType;
import io.temporal.api.history.v1.HistoryEvent;
import io.temporal.client.WorkflowClient;
import io.temporal.testing.TestWorkflowEnvironment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@ContextConfiguration(classes = { TestWorkflowEnvironmentFactory.class, TemporalWorkerConfiguration.class })
@EnableConfigurationProperties
@VisibleForTesting
@SuppressWarnings("java:S5803") // suppress `@VisibleForTesting` warning
public abstract class WorkflowIntegrationTest {

    protected abstract Class<?>[] workflowTypesUnderTest();
    protected abstract Supplier<String> workflowId();

    @Autowired
    private TestWorkflowEnvironmentFactory environmentFactory;

    protected TestWorkflowEnvironment testWorkflowEnvironment;
    protected WorkflowClient workflowClient;

    @BeforeEach
    void testWorkflowEnvironmentSetup() {
        testWorkflowEnvironment = environmentFactory.get(workflowTypesUnderTest());
        workflowClient = testWorkflowEnvironment.getWorkflowClient();

        testWorkflowEnvironment.start();
    }

    @AfterEach
    void testWorkflowEnvironmentTearDown() {
        testWorkflowEnvironment.shutdown();
    }

    protected void runWorkflowEventAndWaitToFinish(Runnable runnable) {
        runnable.run();

        waitUntilTaskFinishes();
    }

    protected void waitUntilWorkflowFinishes() {
        while(!workflowFinished()) {
            sleep();
        }
    }

    private void waitUntilTaskFinishes() {
        while (!taskFinished()) {
            sleep();
        }
    }

    private boolean workflowFinished() {
        EventType lastEventType = workflowClient.fetchHistory(workflowId().get()).getLastEvent().getEventType();
        return temporalEventTypesThatRepresentWorkflowCompletion.contains(lastEventType);
    }

    protected void sleep() {
        testWorkflowEnvironment.sleep(Duration.ofMillis(10));
    }

    private HistoryEvent lastEvent;
    protected boolean taskFinished() {
        HistoryEvent currentLastEvent = workflowClient.fetchHistory(workflowId().get()).getLastEvent();
        if (currentLastEvent.equals(lastEvent)) {
            return false;
        }

        lastEvent = currentLastEvent;
        return temporalEventTypesThatRepresentTaskCompletion.contains(lastEvent.getEventType());
    }

    private static final List<EventType> temporalEventTypesThatRepresentTaskCompletion = List.of(
            EventType.EVENT_TYPE_WORKFLOW_TASK_COMPLETED,
            EventType.EVENT_TYPE_WORKFLOW_EXECUTION_CANCELED,
            EventType.EVENT_TYPE_WORKFLOW_EXECUTION_COMPLETED
    );

    private static final List<EventType> temporalEventTypesThatRepresentWorkflowCompletion = List.of(
            EventType.EVENT_TYPE_WORKFLOW_EXECUTION_CANCELED,
            EventType.EVENT_TYPE_WORKFLOW_EXECUTION_COMPLETED,
            EventType.EVENT_TYPE_WORKFLOW_EXECUTION_FAILED
    );
}
