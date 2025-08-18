package com.yitiankeji.executor.controller;

import com.yitiankeji.executor.JobExecutor;
import com.yitiankeji.job.api.JobApi;
import com.yitiankeji.job.response.HttpResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * DemoController 控制器类
 * 提供了与任务执行相关的HTTP接口
 */
@RestController
public class DemoController {

    @Resource
    private JobExecutor jobExecutor;
    @Resource
    private JobApi jobApi;

    /**
     * 演示接口
     * 返回任务执行器的字符串表示
     *
     * @return 返回jobExecutor的toString()结果
     */
    @RequestMapping("/demo")
    public String demo() {
        return jobExecutor.toString();
    }

    /**
     * 启动任务接口
     * 根据任务ID启动指定任务
     *
     * @param jobId 任务ID
     * @return 返回操作结果(HttpResult)
     */
    @RequestMapping("/start")
    public HttpResult start(Integer jobId) {
        return jobApi.start(jobId);
    }

    /**
     * 停止任务接口
     * 根据任务ID停止指定任务
     *
     * @param jobId 任务ID
     * @return 返回操作结果(HttpResult)
     */
    @RequestMapping("/stop")
    public HttpResult stop(Integer jobId) {
        return jobApi.stop(jobId);
    }
}
