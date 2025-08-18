package com.yitiankeji.job.config;

import com.yitiankeji.job.JobAdminConfig;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ComponentScan("com.yitiankeji.job")
@EnableConfigurationProperties(JobAdminConfig.class)
public class JobAutoConfiguration {

    @Bean
    public JobAdminConfig jobAdminConfig() {
        return new JobAdminConfig();
    }
}
