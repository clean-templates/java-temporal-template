package com.rolandsall;

import io.temporal.failure.ApplicationFailure;
import io.temporal.failure.TemporalException;
import io.temporal.failure.TemporalFailure;

public class ExceptionUtils {
    private ExceptionUtils() {
    }

    public static ApplicationFailure wrapActivityFailure(Exception exception) {
        return ApplicationFailure.newFailureWithCause(exception.getMessage(), exception.getClass().toString(), exception);
    }

    public static ApplicationFailure wrapWorkflowFailure(Exception exception) {
        return ApplicationFailure.newNonRetryableFailureWithCause(exception.getMessage(), exception.getClass().toString(), exception);
    }

    public static String extractFailureMessage(Throwable exception) {
        if (isNotATemporalException(exception)) {
            return exception.getMessage();
        }
        if (isATemporalException(exception) && wrapsAnException(exception)) {
            return extractFailureMessage(exception.getCause());
        }
        if(exception instanceof TemporalFailure exceptionAsApplicationFailure){
            return exceptionAsApplicationFailure.getOriginalMessage();
        }
        return exception.getMessage();
    }

    public static <E extends Exception> boolean isStrictInstanceOf(Throwable exception, Class<E> exceptionClass) {
        if (isATemporalException(exception)) {
            TemporalException temporalException = (TemporalException) exception;
            if (wrapsAnException(temporalException)) {
                return isStrictInstanceOf(temporalException.getCause(), exceptionClass);
            }
            return applicationFailureTypeMatches(temporalException, exceptionClass);
        } else {
            return exception.getClass().equals(exceptionClass);
        }
    }

    private static <E extends Exception> boolean applicationFailureTypeMatches(TemporalException temporalException, Class<E> exceptionClass) {
        if (isNotTemporalApplicationFailure(temporalException)) {
            throw new IllegalArgumentException("Leaf node is expected to be of type application failure");
        }
        return ((ApplicationFailure) temporalException).getType().equals(exceptionClass.getName());
    }

    private static boolean isNotATemporalException(Throwable exception) {
        return !isATemporalException(exception);
    }

    private static boolean isNotTemporalApplicationFailure(TemporalException temporalException) {
        return !isATemporalApplicationFailure(temporalException);
    }

    private static boolean isATemporalApplicationFailure(TemporalException temporalException) {
        return temporalException instanceof ApplicationFailure;
    }

    private static boolean wrapsAnException(Throwable exception) {
        return exception.getCause() != null;
    }

    private static boolean isATemporalException(Throwable exception) {
        return exception instanceof TemporalException;
    }
}
