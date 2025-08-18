package com.yitiankeji.job.model;

import lombok.Data;

/** 任务 **/
@Data
public class Job {
    /** 任务ID **/
    private Integer jobId;
    /** 执行器编码 **/
    private String jobExecutorCode;
    /** 执行器名称 **/
    private String jobExecutorName;
    /** 任务名称 **/
    private String name;
    /** 任务分类 **/
    private String category;
    /** 调度表达式 **/
    private String cron;
    /** 任务处理器 **/
    private String executorHandler;
    /** 任务参数 **/
    private String executorParams;
    /** 路由规则(1第一个、2最后一个、3轮询、4随机、5分片广播) **/
    private String routeRule;
    /** 阻塞处理策略(1单机串行、2丢弃后续调度、3覆盖之前调度) **/
    private String blockStrategy;
    /** 任务超时时间(毫秒) **/
    private String timeout;
    /** 失败重试次数 **/
    private String retryTimes;
    /** 启动状态(0未启动、1已启动) **/
    private String status;
}
