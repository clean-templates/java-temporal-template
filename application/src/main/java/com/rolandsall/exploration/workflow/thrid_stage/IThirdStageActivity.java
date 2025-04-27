package com.rolandsall.exploration.workflow.thrid_stage;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface IThirdStageActivity {

    @ActivityMethod
    void execute3New();

    @ActivityMethod
    void execute3Old();
}
