package com.poc.exploration;

import com.google.common.annotations.VisibleForTesting;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.converter.DataConverter;
import io.temporal.spring.boot.ActivityImpl;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.spring.boot.autoconfigure.properties.WorkerProperties;
import io.temporal.testing.TestEnvironmentOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@VisibleForTesting
@SuppressWarnings("java:S5803") // suppress `@VisibleForTesting` warning
public class TestWorkflowEnvironmentFactory {

    private final ApplicationContext context;

    public TestWorkflowEnvironment get(Class<?>... workflowTypesUnderTest) {
        TestWorkflowEnvironment testWorkflowEnvironment = TestWorkflowEnvironment.newInstance();

        registerActivities(testWorkflowEnvironment);

        registerMockWorkflows(testWorkflowEnvironment);

        registerWorkflowTypesUnderTest(workflowTypesUnderTest, testWorkflowEnvironment);

        return testWorkflowEnvironment;
    }

    private static void registerWorkflowTypesUnderTest(Class<?>[] workflowTypesUnderTest, TestWorkflowEnvironment testWorkflowEnvironment) {
        Arrays.stream(workflowTypesUnderTest).forEach(workflowType ->
                getWorkflowTaskQueues(workflowType).forEach(taskQueue ->
                        testWorkflowEnvironment.newWorker(taskQueue).registerWorkflowImplementationTypes(workflowType)));
    }

    private void registerMockWorkflows(TestWorkflowEnvironment testWorkflowEnvironment) {
        Map<String, Object> mockWorkflows = context.getBeansWithAnnotation(WorkflowImpl.class);
        mockWorkflows.values().forEach(workflow ->
                getWorkflowTaskQueues(workflow).forEach(taskQueue ->
                        testWorkflowEnvironment.newWorker(taskQueue).registerWorkflowImplementationFactory(getWorkflowInterface(workflow), () -> workflow)));
    }

    @SuppressWarnings("unchecked")
    private Class<Object> getWorkflowInterface(Object workflow) {
        return (Class<Object>) workflow.getClass().getInterfaces()[0];
    }

    private static List<String> getWorkflowTaskQueues(Object workflow) {
        return getWorkflowTaskQueues(workflow.getClass());
    }

    private static List<String> getWorkflowTaskQueues(Class<?> workflowType) {
        return Arrays.stream(workflowType.getAnnotation(WorkflowImpl.class).taskQueues()).toList();
    }

    private void registerActivities(TestWorkflowEnvironment testWorkflowEnvironment) {
        registerBeanActivities(testWorkflowEnvironment);

    }

    private void registerBeanActivities(TestWorkflowEnvironment testWorkflowEnvironment) {
        Map<String, Object> mockActivities = context.getBeansWithAnnotation(ActivityImpl.class);
        mockActivities.values().forEach(activity ->
                getActivityTaskQueues(activity).forEach(taskQueue ->
                        registerActivity(testWorkflowEnvironment, taskQueue, activity)));
    }


    private void registerActivity(TestWorkflowEnvironment testWorkflowEnvironment, String taskQueue, Object activity) {
        testWorkflowEnvironment.newWorker(taskQueue).registerActivitiesImplementations(activity);
    }

    private static List<String> getActivityTaskQueues(Object activity) {
        ActivityImpl annotation = activity.getClass().getAnnotation(ActivityImpl.class);
        return Arrays.stream(annotation.taskQueues()).toList();
    }


}
