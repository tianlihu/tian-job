package com.yitiankeji.executor.controller;

import com.yitiankeji.executor.JobExecutor;
import com.yitiankeji.executor.request.JobRequest;
import com.yitiankeji.executor.response.HttpResponse;
import com.yitiankeji.executor.service.JobExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务执行控制器
 * 提供任务的健康检测、执行和终止等API接口
 */
@Slf4j
@RestController
@RequestMapping("/jobExecutor")
public class JobExecutorController {

    @Resource
    private JobExecutorService jobExecutorService;
    @Resource
    private JobExecutor jobExecutor;

    /** 健康检测 **/
    @PostMapping("/health")
    public HttpResponse<String> health() {
        if (log.isDebugEnabled()) {
            log.debug("收到来自[{}]的心跳消息", jobExecutor.getAdminAddresses());
        }
        return HttpResponse.success();
    }

    /** 执行任务 **/
    @PostMapping("/run")
    public HttpResponse<String> run(@RequestBody JobRequest request) {
        return jobExecutorService.run(request);
    }

    /** 终止任务 **/
    @PostMapping("/kill")
    public HttpResponse<String> kill(@RequestBody JobRequest request) {
        return jobExecutorService.kill(request);
    }
}
