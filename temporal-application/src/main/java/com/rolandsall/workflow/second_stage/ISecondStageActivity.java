package com.rolandsall.workflow.second_stage;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ISecondStageActivity {

    @ActivityMethod
    void execute2();
}
