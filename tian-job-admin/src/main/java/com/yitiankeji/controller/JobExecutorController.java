package com.yitiankeji.controller;

import com.yitiankeji.model.JobExecutor;
import com.yitiankeji.query.JobExecutorQuery;
import com.yitiankeji.request.ExecutorRequest;
import com.yitiankeji.response.HttpResponse;
import com.yitiankeji.service.JobExecutorService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/** 调度器管理 **/
@RestController
@RequestMapping("/jobExecutor")
public class JobExecutorController {

    @Resource
    private JobExecutorService jobExecutorService;

    /** 查询调度器列表 **/
    @PostMapping("/page")
    public Page<JobExecutor> page(JobExecutorQuery query) {
        return jobExecutorService.page(query);
    }

    /** 添加调度器 **/
    @PostMapping("/save")
    public HttpResponse<String> save(@RequestBody JobExecutor jobExecutor) {
        jobExecutorService.save(jobExecutor);
        return HttpResponse.success();
    }

    /** 修改调度器 **/
    @PostMapping("/update")
    public HttpResponse<String> update(@RequestBody JobExecutor jobExecutor) {
        jobExecutorService.update(jobExecutor);
        return HttpResponse.success();
    }

    /** 删除调度器 **/
    @PostMapping("/delete")
    public HttpResponse<String> delete(@RequestParam String jobExecutorCode) {
        jobExecutorService.delete(jobExecutorCode);
        return HttpResponse.success();
    }

    /** 注册执行器 **/
    @PostMapping("/register")
    public HttpResponse<String> register(@RequestBody ExecutorRequest request) {
        jobExecutorService.register(request);
        return HttpResponse.success();
    }

    /** 注销执行器 **/
    @PostMapping("/unregister")
    public HttpResponse<String> unregister(@RequestBody ExecutorRequest request) {
        jobExecutorService.unregister(request);
        return HttpResponse.success();
    }
}
