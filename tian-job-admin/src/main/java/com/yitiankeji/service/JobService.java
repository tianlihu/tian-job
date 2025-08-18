package com.yitiankeji.service;

import com.yitiankeji.dao.JobDao;
import com.yitiankeji.dao.JobLogDao;
import com.yitiankeji.exception.JobException;
import com.yitiankeji.model.Job;
import com.yitiankeji.query.JobQuery;
import com.yitiankeji.task.JobTask;
import com.yitiankeji.view.JobView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import static com.yitiankeji.constants.Constants.JOB_STATUS_RUNNING;
import static com.yitiankeji.constants.Constants.JOB_STATUS_STOP;

/**
 * 任务服务类
 * 提供任务的增删改查、启动、停止、触发等功能
 */
@Slf4j
@Service
public class JobService {

    /**
     * 存储已调度的任务集合
     * key: 任务ID
     * value: 调度任务对象
     */
    private final Map<Long, ScheduledFuture<?>> scheduledJobs = new ConcurrentHashMap<>();

    @Resource
    private JobDao jobDao;
    @Resource
    private JobLogDao jobLogDao;
    @Resource
    private ThreadPoolTaskScheduler jobScheduler;
    @Resource
    private ApplicationContext applicationContext;

    /**
     * 分页查询任务列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    public Page<JobView> page(JobQuery query) {
        return jobDao.page(query, PageRequest.of(query.getPage() - 1, query.getPageSize()));
    }

    /**
     * 保存新任务
     *
     * @param job 任务信息
     * @return 任务ID
     */
    public Long save(Job job) {
        Job dbJob = new Job();
        dbJob.setJobExecutorCode(job.getJobExecutorCode());
        dbJob.setName(job.getName());
        dbJob.setCategory(job.getCategory());
        dbJob.setCron(job.getCron());
        dbJob.setExecutorHandler(job.getExecutorHandler());
        dbJob.setExecutorParams(job.getExecutorParams());
        dbJob.setRouteRule(job.getRouteRule());
        dbJob.setBlockStrategy(job.getBlockStrategy());
        dbJob.setTimeout(job.getTimeout());
        dbJob.setRetryTimes(job.getRetryTimes());
        dbJob.setStatus(job.getStatus());
        jobDao.saveAndFlush(dbJob);
        return dbJob.getJobId();
    }

    /**
     * 更新任务信息
     *
     * @param job 任务信息
     * @return 任务ID
     */
    public Long update(Job job) {
        Job dbJob = findById(job.getJobId());
        dbJob.setJobId(job.getJobId());
        dbJob.setJobExecutorCode(job.getJobExecutorCode());
        dbJob.setName(job.getName());
        dbJob.setCategory(job.getCategory());
        dbJob.setCron(job.getCron());
        dbJob.setExecutorHandler(job.getExecutorHandler());
        dbJob.setExecutorParams(job.getExecutorParams());
        dbJob.setRouteRule(job.getRouteRule());
        dbJob.setBlockStrategy(job.getBlockStrategy());
        dbJob.setTimeout(job.getTimeout());
        dbJob.setRetryTimes(job.getRetryTimes());
        jobDao.saveAndFlush(dbJob);

        // 如果任务正在运行，需要重启任务
        if (JOB_STATUS_RUNNING.equals(dbJob.getStatus())) {
            stop(dbJob.getJobId());
            start(dbJob.getJobId());
        }
        return dbJob.getJobId();
    }

    /**
     * 删除任务
     *
     * @param jobId 任务ID
     * @return 任务ID
     */
    public Long delete(Long jobId) {
        stop(jobId);  // 先停止任务
        jobDao.deleteById(jobId);
        jobLogDao.deleteByJobId(jobId);
        return jobId;
    }

    /**
     * 启动任务
     *
     * @param jobId 任务ID
     */
    public void start(Long jobId) {
        Job dbJob = findById(jobId);
        if (JOB_STATUS_RUNNING.equals(dbJob.getStatus())) {
            throw new JobException("任何已经启动，不能重复启动");
        }

        addJobTask(dbJob);
        dbJob.setStatus(JOB_STATUS_RUNNING);
        jobDao.saveAndFlush(dbJob);
    }

    /**
     * 停止指定ID的任务
     *
     * @param jobId 任务ID
     */
    public void stop(Long jobId) {
        // 从已调度任务映射中获取任务
        ScheduledFuture<?> scheduledTask = scheduledJobs.get(jobId);
        if (scheduledTask != null) {
            // 如果任务存在，取消任务并从映射中移除
            scheduledTask.cancel(true);
            scheduledJobs.remove(jobId);
        }

        // 从数据库查找任务
        Job dbJob = findById(jobId);
        // 设置任务状态为停止
        dbJob.setStatus(JOB_STATUS_STOP);
        // 保存任务状态到数据库
        jobDao.saveAndFlush(dbJob);
    }

    /**
     * 立即触发指定ID的任务执行
     *
     * @param jobId 任务ID
     */
    public void trigger(Long jobId) {
        // 从数据库查找任务
        Job dbJob = findById(jobId);
        // 创建并立即运行任务
        createJobTask(dbJob).run();
    }

    /**
     * 添加新的任务调度
     *
     * @param job 要添加的任务
     */
    public synchronized void addJobTask(Job job) {
        // 检查任务是否已经存在
        ScheduledFuture<?> scheduledTask = scheduledJobs.get(job.getJobId());
        if (scheduledTask != null) {
            return; // 如果任务已存在，直接返回
        }
        // 创建新的定时任务并添加到调度器
        scheduledTask = jobScheduler.schedule(createJobTask(job), new CronTrigger(job.getCron()));
        // 将任务添加到已调度任务映射中
        scheduledJobs.put(job.getJobId(), scheduledTask);
    }

    /**
     * 根据任务ID从数据库查找任务
     *
     * @param jobId 任务ID
     * @return 找到的任务对象
     * @throws JobException 如果任务不存在
     */
    private Job findById(Long jobId) {
        // 从数据库查找任务
        Job dbJob = jobDao.findById(jobId).orElse(null);
        if (dbJob == null) {
            // 如果任务不存在，抛出异常
            throw new JobException("任务不存在：" + jobId);
        }
        return dbJob;
    }

    /**
     * 创建任务执行对象
     *
     * @param job 任务信息
     * @return 配置好的任务执行对象
     */
    private JobTask createJobTask(Job job) {
        // 获取JobTask实例
        JobTask jobTask = applicationContext.getBean(JobTask.class);
        // 设置任务信息
        jobTask.setJob(job);
        return jobTask;
    }
}
