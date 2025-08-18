package com.yitiankeji.task;

import com.yitiankeji.dao.JobExecutorDao;
import com.yitiankeji.dao.JobLogDao;
import com.yitiankeji.model.Job;
import com.yitiankeji.model.JobExecutor;
import com.yitiankeji.model.JobLog;
import com.yitiankeji.response.HttpResult;
import com.yitiankeji.utils.HttpUtils;
import com.yitiankeji.utils.Maps;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.yitiankeji.constants.Constants.URL_RUN;

/**
 * 任务执行类
 * 实现Runnable接口，用于异步执行定时任务
 */
@Slf4j
@Data
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JobTask implements Runnable {

    @Resource
    private JobExecutorDao jobExecutorDao;
    @Resource
    private JobLogDao jobLogDao;

    @Setter
    private Job job;
    /** 当前执行器索引 */
    private int current;

    /**
     * 任务执行方法
     * 实现Runnable接口的run方法，用于执行具体任务
     */
    @Override
    public void run() {
        // 根据任务执行器代码查找执行器信息
        JobExecutor executor = jobExecutorDao.findById(job.getJobExecutorCode()).orElse(null);
        if (executor == null) {
            log.error("没有找到执行器：{}", job.getJobExecutorCode());
            return;
        }

        // 获取执行器地址列表
        List<String> executorAddresses = executor.getExecutorAddressList();
        if (executorAddresses.isEmpty()) {
            log.error("没有找到执行器：{}", executor.getJobExecutorCode());
            return;
        }

        // 如果当前索引超过执行器地址列表大小，重置为0
        if (current >= executorAddresses.size()) {
            current = 0;
        }

        // 记录触发时间
        Date triggerTime = new Date();
        // 获取当前执行器地址
        String executorAddress = executorAddresses.get(current);
        // 默认触发代码和消息
        String triggerCode = "success";
        String triggerMessage = "执行任务成功";
        try {
            // 发送HTTP请求执行任务
            HttpResult result = HttpUtils.post(executorAddress + URL_RUN, null, Maps.of("jobId", job.getJobId(), "executorHandler", job.getExecutorHandler(), "executorParams", job.getExecutorParams()));
            if (result.isError()) {
                triggerCode = "error";
                triggerMessage = "执行任务出错: " + result.getErrorMessage();
                log.error("执行任务出错：{}，{}", job.getJobId(), result.getErrorMessage());
            } else {
                log.debug("执行任务成功");
            }
        } catch (Exception e) {
            triggerCode = "error";
            triggerMessage = "执行任务出错: " + ExceptionUtils.getStackTrace(e);
            log.error("执行任务出错：{}", job.getJobId(), e);
        }

        // 创建任务日志对象
        JobLog jobLog = new JobLog();
        jobLog.setJobId(job.getJobId());
        jobLog.setJobExecutorCode(job.getJobExecutorCode());
        jobLog.setExecutorAddress(executorAddress);
        jobLog.setExecutorHandler(job.getExecutorHandler());
        jobLog.setExecutorParams(job.getExecutorParams());
        jobLog.setExecutorFailRetryCount(0);
        jobLog.setRouteRule("3"); // 轮询
        jobLog.setTriggerTime(triggerTime);
        jobLog.setTriggerCode(triggerCode);
        jobLog.setTriggerMessage(triggerMessage);
        // 保存任务日志
        jobLogDao.saveAndFlush(jobLog);

        // 当前执行器索引递增
        current++;
    }
}
