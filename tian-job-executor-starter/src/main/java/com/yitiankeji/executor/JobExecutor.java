package com.yitiankeji.executor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** 任务调度管理 **/
@Data
@ConfigurationProperties(prefix = "tian.job")
public class JobExecutor {

    /** 任务调度管理平台的地址 **/
    private String adminAddresses;
    /** 执行器编码 **/
    private String jobExecutorCode;
    /** 当前执行器的 IP 地址。如果设置为空字符串，XXL-JOB 执行器会自动检测本机 IP。通常情况下，可以将其设置为空，让系统自动检测。 **/
    private String jobExecutorIp;
}
