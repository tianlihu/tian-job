package com.yitiankeji.executor.request;

import lombok.Data;

@Data
public class JobRequest {

    /** 任务ID **/
    private Integer jobId;
    /** 任务处理器 **/
    private String executorHandler;
    /** 任务参数 **/
    private String executorParams;
}
