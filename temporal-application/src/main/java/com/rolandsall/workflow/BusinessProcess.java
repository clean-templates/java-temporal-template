package com.rolandsall.workflow;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface BusinessProcess {

    @WorkflowMethod
    void execute();

    @SignalMethod
    void passFirstStage();

    @SignalMethod
    void passSecondStage();
}
