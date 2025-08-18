package com.yitiankeji.listener;

import com.yitiankeji.dao.JobDao;
import com.yitiankeji.dao.JobLockDao;
import com.yitiankeji.job.HealthJob;
import com.yitiankeji.model.Job;
import com.yitiankeji.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * 应用程序启动监听器
 * 用于在Spring Boot应用启动完成后执行初始化操作
 * 包括获取任务锁、健康检查、批量启动未完成的任务等
 */
@Slf4j
@Component
public class ApplicationListener {

    @Resource
    private JobDao jobDao;
    @Resource
    private JobLockDao jobLockDao;
    @Resource
    private JobService jobService;
    @Resource
    private HealthJob healthJob;

    /** 监听ApplicationReadyEvent事件，初始化消费者 **/
    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("正在启动应用中.....");
        log.info("正在获取任务锁中.....");
        jobLockDao.lock();
        log.info("获取任务锁成功.....");

        healthJob.healthCheck();

        List<Job> runningJobs = jobDao.selectRunningJobs();
        if (runningJobs.isEmpty()) {
            log.info("启动应用成功.....");
            return;
        }

        log.info("正在批量启动任务，共有{}个任务需要启动", runningJobs.size());
        for (Job runningJob : runningJobs) {
            try {
                jobService.addJobTask(runningJob);
            } catch (Exception e) {
                log.error("启动任务失败，任务ID：{}", runningJob.getJobId(), e);
            }
        }
        log.info("批量启动任务成功，共启动{}个任务", runningJobs.size());

        log.info("启动应用成功.....");
    }
}
