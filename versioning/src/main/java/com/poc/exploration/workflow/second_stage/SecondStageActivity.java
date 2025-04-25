package com.poc.exploration.workflow.second_stage;

import com.poc.exploration.ExceptionUtils;
import com.poc.exploration.config.TaskQueues;
import io.temporal.spring.boot.ActivityImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@ActivityImpl(taskQueues = {TaskQueues.BUSINESS_PROCESS})
@Slf4j
public class SecondStageActivity implements ISecondStageActivity {

    @Override
    public void execute2() {
        try {
            log.info("Activity of Second Stage Is Executed");
        } catch (Exception e) {
            throw ExceptionUtils.wrapActivityFailure(e);
        }
    }
}
