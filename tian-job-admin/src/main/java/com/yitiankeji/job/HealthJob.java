package com.yitiankeji.job;

import com.yitiankeji.model.JobExecutor;
import com.yitiankeji.request.ExecutorRequest;
import com.yitiankeji.response.HttpResult;
import com.yitiankeji.service.JobExecutorService;
import com.yitiankeji.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.yitiankeji.constants.Constants.URL_HEALTH;

/**
 * 健康检查任务类
 * 用于定期检查执行器的健康状态，对不健康的执行器进行剔除
 */
@Slf4j
@Component
public class HealthJob {

    @Resource
    private JobExecutorService jobExecutorService;  // 注入执行器服务

    /**
     * 健康检查定时任务
     * 每30秒执行一次，检查所有执行器的健康状态
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void healthCheck() {
        // 获取所有执行器列表
        List<JobExecutor> jobExecutors = jobExecutorService.list();
        if (log.isDebugEnabled()) {
            log.debug("开始执行健康检查，共有{}个执行器需要进行健康检查", jobExecutors.size());
        }
        // 遍历所有执行器进行健康检查
        for (JobExecutor jobExecutor : jobExecutors) {
            List<String> executorAddresses = jobExecutor.getExecutorAddressList();
            // 如果执行器地址列表为空，则跳过
            if (executorAddresses.isEmpty()) {
                continue;
            }
            // 遍历执行器的所有地址进行健康检查
            for (String executorAddress : executorAddresses) {
                try {
                    healthCheck(jobExecutor, executorAddress);
                } catch (Exception e) {
                    log.error("健康检查出错：{}", executorAddress, e);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("健康检查结束");
        }
    }

    /**
     * 对单个执行器地址进行健康检查
     *
     * @param jobExecutor     执行器信息
     * @param executorAddress 执行器地址
     */
    private void healthCheck(JobExecutor jobExecutor, String executorAddress) {
        // 发送健康检查请求
        HttpResult result = HttpUtils.post(executorAddress + URL_HEALTH, null, Collections.emptyMap());
        // 如果请求正常，则直接返回
        if (!result.isError()) {
            return;
        }

        // 记录错误日志，并从执行器列表中移除不健康的执行器
        log.error("执行器健康检查不通过，强制在执行器列表剔除{}", executorAddress);
        ExecutorRequest request = new ExecutorRequest();
        request.setJobExecutorCode(jobExecutor.getJobExecutorCode());
        request.setExecutorAddress(executorAddress);
        jobExecutorService.unregister(request);
        log.info("强制在执行器列表剔除成功：{}", executorAddress);
    }
}
