package com.rolandsall.exploration.api;

import com.rolandsall.exploration.config.TaskQueues;
import com.rolandsall.exploration.workflow.BusinessProcess;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("business-process")
@Slf4j
public class WorkflowController {

    private final WorkflowClient workflowClient;

    @GetMapping("/execute")
    public ResponseEntity<String> execute() {
        String workflowId = "bp-execution-" + UUID.randomUUID();
        WorkflowStub businessProcess = workflowClient.newUntypedWorkflowStub("BusinessProcess", getWorkflowOptions(workflowId));
        businessProcess.start();
        return ResponseEntity.ok(workflowId);
    }

    @GetMapping("/firstSignal/{workflowId}")
    public void sendSignalForFirstStage(@PathVariable String workflowId) {
        log.info("received send signal for first stage request with workflow id {}", workflowId);
        BusinessProcess businessProcess = workflowClient.newWorkflowStub(BusinessProcess.class, workflowId);
        businessProcess.passFirstStage();
    }

    @GetMapping("/secondSignal/{workflowId}")
    public void sendSignalForSecondStage(@PathVariable String workflowId) {
        log.info("received send signal for second stage request with workflow id {}", workflowId);
        BusinessProcess businessProcess = workflowClient.newWorkflowStub(BusinessProcess.class, workflowId);
        businessProcess.passSecondStage();
    }


    private WorkflowOptions getWorkflowOptions(String workflowId) {
        return WorkflowOptions.newBuilder()
                .setWorkflowId(workflowId)
                .setTaskQueue(TaskQueues.BUSINESS_PROCESS)
                .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE)
                .build();
    }
}
