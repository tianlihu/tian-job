package com.yitiankeji.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 任务线程配置类
 * 用于配置和创建线程池任务调度器
 */
@Configuration
public class TaskThreadConfig {

    /**
     * 创建并配置线程池任务调度器Bean
     *
     * @return 配置好的ThreadPoolTaskScheduler实例
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        // 创建线程池任务调度器实例
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 设置线程名称前缀，方便在日志和监控中识别
        scheduler.setThreadNamePrefix("Thread-Pool-Task-");
        // 设置线程池大小，这里设置为1000个线程
        scheduler.setPoolSize(1000);
        // 初始化调度器
        scheduler.initialize();
        // 返回配置好的调度器实例
        return scheduler;
    }
}
