package com.poc.exploration.workflow.createbranch;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ICreateBranchActivity {

    @ActivityMethod
    void createDevelopment();
}
