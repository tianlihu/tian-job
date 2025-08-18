package com.yitiankeji.executor.scheduler;

import com.yitiankeji.executor.service.JobExecutorRegister;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class HeartbeatScheduler {

    @Resource
    private JobExecutorRegister jobExecutorRegister;

    @Scheduled(cron = "0/30 * * * * ?")
    public void heartbeat() {
        jobExecutorRegister.register();
    }
}
