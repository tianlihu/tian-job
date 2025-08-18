package com.yitiankeji.request;

import lombok.Data;

@Data
public class ExecutorRequest {

    /** 执行器编码 **/
    private String jobExecutorCode;
    /** 执行器地址 **/
    private String executorAddress;
}
