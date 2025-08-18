package com.yitiankeji.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/** 任务执行日志 **/
@Data
@Entity
@Table(name = "TIAN_JOB_LOG")
public class JobLog {
    /** 任务执行日志ID **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobLogId;
    /** 任务ID **/
    private Long jobId;
    /** 执行器编码 **/
    private String jobExecutorCode;
    /** 执行器地址，本次执行的地址 **/
    private String executorAddress;
    /** 任务处理器 **/
    private String executorHandler;
    /** 任务参数 **/
    private String executorParams;
    /** 失败重试次数 **/
    private Integer executorFailRetryCount;
    /** 路由规则(1第一个、2最后一个、3轮询、4随机、5分片广播) **/
    private String routeRule;
    /** 阻塞处理策略(1单机串行、2丢弃后续调度、3覆盖之前调度) **/
    private String blockStrategy;
    /** 任务超时时间(毫秒) **/
    private String timeout;
    /** 调度时间 **/
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date triggerTime;
    /** 调度结果 **/
    private String triggerCode;
    /** 调度日志 **/
    private String triggerMessage;
}
