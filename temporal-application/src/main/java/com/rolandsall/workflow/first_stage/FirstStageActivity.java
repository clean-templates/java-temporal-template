package com.rolandsall.workflow.first_stage;

import com.rolandsall.ExceptionUtils;
import com.rolandsall.config.TaskQueues;
import io.temporal.spring.boot.ActivityImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@ActivityImpl(taskQueues = {TaskQueues.BUSINESS_PROCESS})
@Slf4j
public class FirstStageActivity implements IFirstStageActivity {

    @Override
    public void execute1() {
        try {
            log.info("Activity of First Stage Is Executed");
        } catch (Exception e) {
            throw ExceptionUtils.wrapActivityFailure(e);
        }
    }
}
