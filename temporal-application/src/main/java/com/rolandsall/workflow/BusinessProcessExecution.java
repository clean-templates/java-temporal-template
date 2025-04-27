package com.rolandsall.workflow;

import com.rolandsall.ActivityUtils;
import com.rolandsall.config.TaskQueues;
import com.rolandsall.workflow.createbranch.ICreateBranchActivity;
import com.rolandsall.workflow.first_stage.IFirstStageActivity;
import com.rolandsall.workflow.second_stage.ISecondStageActivity;
import com.rolandsall.workflow.thrid_stage.IThirdStageActivity;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

@WorkflowImpl(taskQueues = TaskQueues.BUSINESS_PROCESS)
public class BusinessProcessExecution implements BusinessProcess {
    private final Logger log = Workflow.getLogger(BusinessProcessExecution.class);

    ICreateBranchActivity createBranchActivity = ActivityUtils.createNonRetryableActivity(ICreateBranchActivity.class);
    IFirstStageActivity firstStageActivity = ActivityUtils.createNonRetryableActivity(IFirstStageActivity.class);
    ISecondStageActivity secondStageActivity = ActivityUtils.createNonRetryableActivity(ISecondStageActivity.class);
    IThirdStageActivity thirdStageActivity = ActivityUtils.createNonRetryableActivity(IThirdStageActivity.class);
    WorkflowState workflowState = new WorkflowState();

    @Override
    public void execute() {
        try {
            log.info("started process");

            createBranch();

            executeStage1();

//            int version = Workflow.getVersion("newActivity", Workflow.DEFAULT_VERSION, 1);
//            log.info("workflow version {}", version);



            executeOldVersionedStage();

            executeStage2();
        } catch (Exception e) {
            log.error("Business Process Failed", e);
        }
    }

    private void createBranch() {
        createBranchActivity.createDevelopment();
    }

    private void executeStage1() {
        firstStageActivity.execute1();
        log.info("waiting for user decision on stage 1");
        Workflow.await(() -> workflowState.waitingForUserDecisionOnStageOne());
        log.info("user made decision on stage 1");

    }

    private void executeStage2() {
        secondStageActivity.execute2();
        log.info("waiting for user decision on stage 2");
        Workflow.await(() -> workflowState.waitingForUserDecisionOnStageTwo());
        log.info("waiting for user decision on stage 2");
    }

    private void executeNewVersionedStage() {
        thirdStageActivity.execute3New();
    }

    private void executeOldVersionedStage() {
        thirdStageActivity.execute3Old();
    }

    @Override
    public void passFirstStage() {
        log.info("received signal for first stage");
        workflowState.userMadeDecisionOnStageOne();
    }

    @Override
    public void passSecondStage() {
        log.info("received signal for second stage");
        workflowState.userMadeDecisionOnStageTwo();
    }
}
