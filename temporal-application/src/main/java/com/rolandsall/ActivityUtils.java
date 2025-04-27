package com.rolandsall;

import io.temporal.activity.ActivityCancellationType;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class ActivityUtils {
    private ActivityUtils() {
    }

    public static <T> T createNonRetryableActivity(Class<T> activityInterface) {
        return Workflow.newActivityStub(activityInterface, ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(300))
                .setRetryOptions(getZeroRetriesOption())
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build());
    }

    public static <T> T createNonRetryableActivity(Class<T> activityInterface, String taskQueue) {
        return Workflow.newActivityStub(activityInterface, ActivityOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setStartToCloseTimeout(Duration.ofSeconds(300))
                .setRetryOptions(getZeroRetriesOption())
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build());
    }

    public static <T> T createRetryableActivity(Class<T> activityInterface, String taskQueue, Duration timeout, int retryAttempts) {
        return Workflow.newActivityStub(activityInterface, ActivityOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setStartToCloseTimeout(timeout)
                .setRetryOptions(getRetriesOption(retryAttempts))
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build());
    }

    public static <T> T createRetryableActivity(Class<T> activityInterface, Duration timeout, int retryAttempts) {
        return Workflow.newActivityStub(activityInterface, ActivityOptions.newBuilder()
                .setStartToCloseTimeout(timeout)
                .setRetryOptions(getRetriesOption(retryAttempts))
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build());
    }

    public static <T> T createRetryableActivity(Class<T> activityInterface, int retryAttempts) {
        return Workflow.newActivityStub(activityInterface, ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(300))
                .setRetryOptions(getRetriesOption(retryAttempts))
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build());
    }

    public static <T> T createPollerActivity(Class<T> activityInterface, String taskQueue, Duration timeout, int initialIntervalInSeconds) {
        return Workflow.newActivityStub(activityInterface, ActivityOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setScheduleToCloseTimeout(timeout)
                .setRetryOptions(getInfiniteRetriesOption(initialIntervalInSeconds))
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build());
    }

    public static <T> T createPollerActivity(Class<T> activityInterface, Duration timeout, int initialIntervalInSeconds) {
        return Workflow.newActivityStub(activityInterface, ActivityOptions.newBuilder()
                .setScheduleToCloseTimeout(timeout)
                .setRetryOptions(getInfiniteRetriesOption(initialIntervalInSeconds))
                .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build());
    }

    private static RetryOptions getRetriesOption(int retryAttempts) {
        int totalCountOfAttempts = retryAttempts + 1;
        return RetryOptions.newBuilder()
                .setMaximumAttempts(totalCountOfAttempts)
                .setBackoffCoefficient(2)
                .setInitialInterval(Duration.ofSeconds(1))
                .build();
    }

    private static RetryOptions getInfiniteRetriesOption(int initialIntervalInSeconds) {
        return RetryOptions.newBuilder()
                .setBackoffCoefficient(1)
                .setInitialInterval(Duration.ofSeconds(initialIntervalInSeconds))
                .build();
    }


    private static RetryOptions getZeroRetriesOption() {
        return RetryOptions.newBuilder().setMaximumAttempts(1).build();
    }
}
