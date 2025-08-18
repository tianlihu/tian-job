package com.yitiankeji.job.api;

import com.yitiankeji.job.JobAdminConfig;
import com.yitiankeji.job.model.JobExecutor;
import com.yitiankeji.job.response.HttpResult;
import com.yitiankeji.job.utils.HttpUtils;
import com.yitiankeji.job.utils.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/** 定时调度器API **/
@Component
public class JobExecutorApi {

    @Resource
    private JobAdminConfig jobAdminConfig;

    /** 添加调度器 **/
    public HttpResult save(JobExecutor jobExecutor) {
        Map<String, Object> json = Maps.of(
                "jobExecutorCode", jobExecutor.getJobExecutorCode(),
                "name", jobExecutor.getName(),
                "registerType", jobExecutor.getRegisterType(),
                "executorAddresses", jobExecutor.getExecutorAddresses()
        );
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/jobExecutor/save", Maps.of(), json);
    }

    /** 修改调度器 **/
    public HttpResult update(JobExecutor jobExecutor) {
        Map<String, Object> json = Maps.of(
                "jobExecutorCode", jobExecutor.getJobExecutorCode(),
                "name", jobExecutor.getName(),
                "registerType", jobExecutor.getRegisterType(),
                "executorAddresses", jobExecutor.getExecutorAddresses()
        );
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/jobExecutor/update", Maps.of(), json);
    }

    /** 删除调度器 **/
    public HttpResult delete(String jobExecutorCode) {
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/jobExecutor/delete", Maps.of(), Maps.of(), "jobExecutorCode", jobExecutorCode);
    }

    /**
     * 注册执行器
     *
     * @param jobExecutorCode 执行器编码
     * @param executorAddress 执行器地址
     **/
    public HttpResult register(String jobExecutorCode, String executorAddress) {
        Map<String, Object> json = Maps.of(
                "jobExecutorCode", jobExecutorCode,
                "executorAddress", executorAddress
        );
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/jobExecutor/register", Maps.of(), json);
    }

    /**
     * 注销执行器
     *
     * @param jobExecutorCode 执行器编码
     * @param executorAddress 执行器地址
     **/
    public HttpResult unregister(String jobExecutorCode, String executorAddress) {
        Map<String, Object> json = Maps.of(
                "jobExecutorCode", jobExecutorCode,
                "executorAddress", executorAddress
        );
        return HttpUtils.post(jobAdminConfig.getAdminAddresses() + "/jobExecutor/unregister", Maps.of(), json);
    }
}
