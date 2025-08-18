package com.yitiankeji.executor.job;

import com.yitiankeji.executor.annotation.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoJob {

    @Job("demoJobHandler")
    public void demoJobHandler(String param) {
        log.debug("执行器被调用：{}", param);
    }
}
