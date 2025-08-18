package com.yitiankeji.job.api;

import com.yitiankeji.job.JobAdminConfig;
import com.yitiankeji.job.model.Job;
import com.yitiankeji.job.response.HttpResult;
import com.yitiankeji.job.utils.HttpUtils;
import com.yitiankeji.job.utils.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/** 定时任务管理API **/
@Component
public class JobApi {

    @Resource
    private JobAdminConfig jobAdminConfig;

    /** 添加定时任务 **/
    public HttpResult save(Job job) {
        Map<String, Object> json = Maps.of(
                "jobId", job.getJobId(),
                "jobExecutorCode", job.getJobExecutorCode(),
                "jobExecutorName", job.getJobExecutorName(),
                "name", job.getName(),
                "category", job.getCategory(),
                "cron", job.getCron(),
                "executorHandler", job.getExecutorHandler(),
                "executorParams", job.getExecutorParams(),
                "routeRule", job.getRouteRule(),
                "blockStrategy", job.getBlockStrategy(),
                "timeout", job.getTimeout(),
                "retryTimes", job.getRetryTimes(),
                "status", job.getStatus()
        );
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/job/save", Maps.of(), json);
    }

    /** 修改定时任务 **/
    public HttpResult update(Job job) {
        Map<String, Object> json = Maps.of(
                "jobId", job.getJobId(),
                "jobExecutorCode", job.getJobExecutorCode(),
                "jobExecutorName", job.getJobExecutorName(),
                "name", job.getName(),
                "category", job.getCategory(),
                "cron", job.getCron(),
                "executorHandler", job.getExecutorHandler(),
                "executorParams", job.getExecutorParams(),
                "routeRule", job.getRouteRule(),
                "blockStrategy", job.getBlockStrategy(),
                "timeout", job.getTimeout(),
                "retryTimes", job.getRetryTimes(),
                "status", job.getStatus()
        );
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/job/update", Maps.of(), json);
    }

    /** 删除定时任务 **/
    public HttpResult delete(Integer jobId) {
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/job/delete", Maps.of(), Maps.of(), "jobId", jobId);
    }

    /** 开始执行定时任务 **/
    public HttpResult start(Integer jobId) {
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/job/start", Maps.of(), Maps.of(), "jobId", jobId);
    }

    /** 停止执行定时任务 **/
    public HttpResult stop(Integer jobId) {
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/job/stop", Maps.of(), Maps.of(), "jobId", jobId);
    }

    /** 手工执行定时任务 **/
    public HttpResult trigger(Integer jobId) {
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/job/trigger", Maps.of(), Maps.of(), "jobId", jobId);
    }
}
