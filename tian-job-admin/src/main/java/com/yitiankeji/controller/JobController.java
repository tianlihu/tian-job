package com.yitiankeji.controller;

import com.yitiankeji.model.Job;
import com.yitiankeji.query.JobQuery;
import com.yitiankeji.response.HttpResponse;
import com.yitiankeji.service.JobService;
import com.yitiankeji.view.JobView;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/** 任务管理 **/
@RestController
@RequestMapping("/job")
public class JobController {

    @Resource
    private JobService jobService;

    /** 查询任务列表 **/
    @PostMapping("/page")
    public Page<JobView> page(JobQuery query) {
        return jobService.page(query);
    }

    /** 添加定时任务 **/
    @PostMapping("/save")
    public HttpResponse<Long> save(@RequestBody Job job) {
        return HttpResponse.success(jobService.save(job));
    }

    /** 修改定时任务 **/
    @PostMapping("/update")
    public HttpResponse<Long> update(@RequestBody Job job) {
        return HttpResponse.success(jobService.update(job));
    }

    /** 删除定时任务 **/
    @PostMapping("/delete")
    public HttpResponse<Long> delete(@RequestParam Long jobId) {
        return HttpResponse.success(jobService.delete(jobId));
    }

    /** 开始执行定时任务 **/
    @PostMapping("/start")
    public HttpResponse<String> start(@RequestParam Long jobId) {
        jobService.start(jobId);
        return HttpResponse.success();
    }

    /** 停止执行定时任务 **/
    @PostMapping("/stop")
    public HttpResponse<String> stop(@RequestParam Long jobId) {
        jobService.stop(jobId);
        return HttpResponse.success();
    }

    /** 手工执行定时任务 **/
    @PostMapping("/trigger")
    public HttpResponse<String> trigger(@RequestParam Long jobId) {
        jobService.trigger(jobId);
        return HttpResponse.success();
    }
}
