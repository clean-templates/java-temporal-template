package com.poc.exploration.workflow;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkflowState {

    private boolean isDecisionMadeForStageOne = false;
    private boolean isDecisionMadeForStageTwo = false;

    public void userMadeDecisionOnStageOne() {
        this.isDecisionMadeForStageOne = true;
    }

    public void userMadeDecisionOnStageTwo() {
        this.isDecisionMadeForStageTwo = true;
    }

    public boolean waitingForUserDecisionOnStageOne() {
        return isDecisionMadeForStageOne;
    }

    public boolean waitingForUserDecisionOnStageTwo() {
        return isDecisionMadeForStageTwo;
    }
}
