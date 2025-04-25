package com.poc.exploration.workflow.createbranch;

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
public class CreateBranchActivity implements ICreateBranchActivity {

    @Override
    public void createDevelopment() {
        try {
            Thread.sleep(2000);
            log.info("Branch Created");
        } catch (Exception e) {
            throw ExceptionUtils.wrapActivityFailure(e);
        }
    }
}
