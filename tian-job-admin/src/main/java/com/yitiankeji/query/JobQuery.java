package com.yitiankeji.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JobQuery extends BaseQuery {

    /** 执行器编码 **/
    private String jobExecutorCode;
    /** 启动状态(0未启动、1已启动) **/
    private String status;
    /** 任务名称 **/
    private String jobName;
    /** 任务处理器 **/
    private String jobHandler;
}
