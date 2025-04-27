package com.rolandsall.workflow.thrid_stage;

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
public class ThirdStageActivity implements IThirdStageActivity {


    @Override
    public void execute3New() {
        try {
            log.info("New Activity of Third Stage Is Executed");
        } catch (Exception e) {
            throw ExceptionUtils.wrapActivityFailure(e);
        }
    }

    @Override
    public void execute3Old() {
        try {
            log.info("Old Activity of Third Stage Is Executed");
        } catch (Exception e) {
            throw ExceptionUtils.wrapActivityFailure(e);
        }
    }
}
