import com.rolandsall.config.TaskQueues;
import com.rolandsall.testing.MockActivity;
import com.rolandsall.testing.WorkflowIntegrationTest;
import com.rolandsall.workflow.BusinessProcess;
import com.rolandsall.workflow.BusinessProcessExecution;
import com.rolandsall.workflow.createbranch.CreateBranchActivity;
import com.rolandsall.workflow.first_stage.FirstStageActivity;
import com.rolandsall.workflow.second_stage.SecondStageActivity;
import com.rolandsall.workflow.thrid_stage.ThirdStageActivity;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.function.Supplier;

import static org.mockito.Mockito.verify;

@SpringBootTest()
@ActiveProfiles("test")
class BusinessProcessExecutionIntegrationTest extends WorkflowIntegrationTest {

    public static final String WORKFLOW_ID = "WORKFLOW_ID";

    @MockActivity
    private FirstStageActivity firstStageActivity;

    @MockActivity
    private SecondStageActivity secondStageActivity;

    @MockActivity
    private ThirdStageActivity thirdStageActivity;

    @MockActivity
    private CreateBranchActivity createBranchActivity;


    @BeforeEach
    void setUp() {

    }


    @Test
    @DisplayName("should delegate correctly to first activity")
    void should_delegate_correctly_to_first_activity() {
        launchProcess();
        verify(createBranchActivity).createDevelopment();
    }

    private void launchProcess() {
        runWorkflowEventAndWaitToFinish(() -> {
            BusinessProcess process = setupWorkflow();
            WorkflowClient.start(process::execute);
        });

    }

    private BusinessProcess setupWorkflow() {
        return workflowClient.newWorkflowStub(BusinessProcess.class, getWorkflowOptions());
    }

    public static WorkflowOptions getWorkflowOptions() {
        return WorkflowOptions.newBuilder()
                .setTaskQueue(TaskQueues.BUSINESS_PROCESS)
                .setWorkflowId(WORKFLOW_ID)
                .setWorkflowExecutionTimeout(Duration.ofMinutes(10))
                .build();
    }


    protected void runWorkflowEventAndWaitToFinish(Runnable runnable) {
        runnable.run();

        waitUntilTaskFinishes();
    }

    private void waitUntilTaskFinishes() {
        while (!taskFinished()) {
            sleep();
        }
    }

    @Override
    protected Class<?>[] workflowTypesUnderTest() {
        return new Class<?>[]{BusinessProcessExecution.class};
    }

    @Override
    protected Supplier<String> workflowId() {
        return () -> WORKFLOW_ID;
    }
}