package com.rolandsall.workflow.first_stage;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface IFirstStageActivity {

    @ActivityMethod
    void execute1();
}
