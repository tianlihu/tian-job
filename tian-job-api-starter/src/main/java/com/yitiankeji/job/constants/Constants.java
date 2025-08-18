package com.yitiankeji.job.constants;

/** 常量类，用于定义系统中使用的各种常量值 */
public class Constants {

    /** 任务状态相关常量 - 停止状态 */
    public static final String JOB_STATUS_STOP = "STOP";
    /** 任务状态相关常量 - 运行状态 */
    public static final String JOB_STATUS_RUNNING = "RUNNING";

    /** 注册类型相关常量 - 自动注册 */
    public static final String REGISTER_TYPE_AUTO = "AUTO";
    /** 注册类型相关常量 - 手动注册 */
    public static final String REGISTER_TYPE_MANUAL = "MANUAL";

    /** 健康检测 **/
    public static final String URL_HEALTH = "/jobExecutor/health";
    /** 执行任务 **/
    public static final String URL_RUN = "/jobExecutor/run";
    /** 终止任务 **/
    public static final String URL_KILL = "/jobExecutor/kill";
}
