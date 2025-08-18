package com.yitiankeji.job;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** 任务调度管理 **/
@Data
@ConfigurationProperties(prefix = "tian.job")
public class JobAdminConfig {

    /** 任务调度管理平台的地址 **/
    private String adminAddresses;
    /** 执行器编码 **/
    private String jobExecutorCode;
}
