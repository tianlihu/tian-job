package com.yitiankeji.job.model;

import lombok.Data;

/** 执行器 **/
@Data
public class JobExecutor {
    /** 执行器编码 **/
    private String jobExecutorCode;
    /** 执行器名称 **/
    private String name;
    /** 注册方式(AUTO自动注册、MANUAL手动录入)，默认为AUTO **/
    private String registerType;
    /** 执行器地址(逗号分隔) **/
    private String executorAddresses;
}
