package com.yitiankeji.executor.service;

import com.yitiankeji.executor.context.JobContext;
import com.yitiankeji.executor.context.MethodJobHandler;
import com.yitiankeji.executor.request.JobRequest;
import com.yitiankeji.executor.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class JobExecutorService {

    @Resource
    private JobContext jobContext;
    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    {
        taskScheduler.setThreadNamePrefix("Executor-Task-");
        taskScheduler.setPoolSize(1000);
        taskScheduler.initialize();
    }

    public HttpResponse<String> run(JobRequest request) {
        MethodJobHandler jobHandler = jobContext.getJobHandler(request.getExecutorHandler());
        if (jobHandler == null) {
            log.warn("任务执行失败[{}]，未找到执行器", request.getExecutorHandler());
            return HttpResponse.error("未找到执行器：" + request.getExecutorHandler());
        }

        log.debug("任务[{}]开始执行", request.getExecutorHandler());
        AtomicReference<Throwable> throwableReference = new AtomicReference<>();
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> {
            try {
                jobHandler.execute(request.getExecutorParams());
            } catch (Exception e) {
                throwableReference.set(e);
            } finally {
                scheduledTasks.remove(request.getExecutorHandler());
            }
        }, new Date()); // 一次性任务立即执行

        try {
            scheduledTasks.put(request.getExecutorHandler(), scheduledTask);
            scheduledTask.get();
            log.debug("任务[{}]执行成功", request.getExecutorHandler());
            return HttpResponse.success();
        } catch (ExecutionException e) {
            throwableReference.set(e);
        } catch (InterruptedException e) {
            log.warn("任务终止[{}]", request.getExecutorHandler());
            return HttpResponse.error("任务终止[" + request.getExecutorHandler() + "]");
        }

        Throwable throwable = throwableReference.get();
        String errorMessage = "执行器[" + request.getExecutorHandler() + "]执行出错：" + ExceptionUtils.getStackTrace(throwable);
        log.error(errorMessage);
        return HttpResponse.error(errorMessage);
    }

    public HttpResponse<String> kill(JobRequest request) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(request.getExecutorHandler());
        if (scheduledTask == null) {
            log.debug("任务[{}]终止成功", request.getExecutorHandler());
            return HttpResponse.success();
        }

        boolean canceled = scheduledTask.cancel(true);
        scheduledTasks.remove(request.getExecutorHandler());

        if (canceled) {
            log.debug("任务[{}]终止成功", request.getExecutorHandler());
            return HttpResponse.success();
        }

        log.info("任务[{}]终止失败", request.getExecutorHandler());
        return HttpResponse.error("任务[" + request.getExecutorHandler() + "]终止失败");
    }
}
