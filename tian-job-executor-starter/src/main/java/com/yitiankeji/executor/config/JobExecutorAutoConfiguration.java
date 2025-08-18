package com.yitiankeji.executor.config;

import com.yitiankeji.executor.JobExecutor;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Data
@Configuration
@ComponentScan("com.yitiankeji.executor")
@EnableConfigurationProperties(JobExecutor.class)
public class JobExecutorAutoConfiguration {

    @Resource
    private JobExecutor executor;

    @Bean
    public JobExecutor jobExecutor() {
        return new JobExecutor();
    }
}
