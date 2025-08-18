package com.yitiankeji.service;

import com.yitiankeji.dao.JobDao;
import com.yitiankeji.dao.JobExecutorDao;
import com.yitiankeji.exception.JobException;
import com.yitiankeji.model.JobExecutor;
import com.yitiankeji.query.JobExecutorQuery;
import com.yitiankeji.request.ExecutorRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.yitiankeji.constants.Constants.REGISTER_TYPE_MANUAL;

/**
 * 任务执行器服务类
 * 提供任务执行器的增删改查、注册/注销等功能
 */
@Service
public class JobExecutorService {

    @Resource
    private JobExecutorDao jobExecutorDao;
    @Resource
    private JobDao jobDao;

    /**
     * 分页查询任务执行器
     *
     * @param query 查询条件
     * @return 分页结果
     */
    public Page<JobExecutor> page(JobExecutorQuery query) {
        return jobExecutorDao.page(query, PageRequest.of(query.getPage() - 1, query.getPageSize()));
    }

    /**
     * 获取所有任务执行器列表
     *
     * @return 任务执行器列表
     */
    public List<JobExecutor> list() {
        return jobExecutorDao.list();
    }

    /**
     * 保存任务执行器
     *
     * @param jobExecutor 任务执行器对象
     */
    public void save(JobExecutor jobExecutor) {
        // 检查执行器是否已存在
        JobExecutor dbJobExecutor = jobExecutorDao.findById(jobExecutor.getJobExecutorCode()).orElse(null);
        if (dbJobExecutor != null) {
            return;
        }
        // 创建新的执行器记录
        dbJobExecutor = new JobExecutor();
        dbJobExecutor.setJobExecutorCode(jobExecutor.getJobExecutorCode());
        dbJobExecutor.setName(jobExecutor.getName());
        dbJobExecutor.setRegisterType(jobExecutor.getRegisterType());
        dbJobExecutor.setExecutorAddresses(jobExecutor.getExecutorAddresses());
        // 设置默认注册类型为自动
        if (StringUtils.isEmpty(dbJobExecutor.getRegisterType())) {
            dbJobExecutor.setRegisterType("AUTO");
        }
        jobExecutorDao.saveAndFlush(dbJobExecutor);
    }

    /**
     * 更新任务执行器信息
     *
     * @param jobExecutor 任务执行器对象
     */
    public void update(JobExecutor jobExecutor) {
        // 检查执行器是否存在
        JobExecutor dbJobExecutor = jobExecutorDao.findById(jobExecutor.getJobExecutorCode()).orElse(null);
        if (dbJobExecutor == null) {
            throw new JobException("执行器不存在：" + jobExecutor.getJobExecutorCode());
        }
        // 更新执行器信息
        dbJobExecutor.setJobExecutorCode(jobExecutor.getJobExecutorCode());
        dbJobExecutor.setName(jobExecutor.getName());
        dbJobExecutor.setRegisterType(jobExecutor.getRegisterType());
        // 如果是手动注册类型，更新执行器地址
        if (REGISTER_TYPE_MANUAL.equals(jobExecutor.getRegisterType())) {
            dbJobExecutor.setExecutorAddresses(jobExecutor.getExecutorAddresses());
        }
        // 设置默认注册类型
        if (StringUtils.isEmpty(dbJobExecutor.getRegisterType())) {
            dbJobExecutor.setRegisterType("AUTO");
        }
        jobExecutorDao.saveAndFlush(jobExecutor);
    }

    /**
     * 删除任务执行器
     *
     * @param jobExecutorCode 执行器编码
     */
    public void delete(String jobExecutorCode) {
        // 检查执行器是否正在使用
        boolean hasJobs = jobDao.hasJobs(jobExecutorCode);
        if (hasJobs) {
            throw new JobException("拒绝删除，该执行器使用中");
        }
        jobExecutorDao.deleteById(jobExecutorCode);
    }

    /**
     * 注册任务执行器
     *
     * @param request 执行器注册请求
     */
    public void register(ExecutorRequest request) {
        JobExecutor jobExecutor = findByCode(request);
        String executorAddresses = StringUtils.replace(StringUtils.trimToEmpty(jobExecutor.getExecutorAddresses()), " ", "");
        // 检查地址是否已注册
        if (executorAddresses.contains(request.getExecutorAddress())) {
            return;
        }
        // 添加新的执行器地址
        if (StringUtils.isEmpty(executorAddresses)) {
            executorAddresses = StringUtils.trim(request.getExecutorAddress());
        } else {
            executorAddresses += "," + StringUtils.trim(request.getExecutorAddress());
        }
        jobExecutor.setExecutorAddresses(executorAddresses);
        jobExecutorDao.saveAndFlush(jobExecutor);
    }

    /**
     * 注销任务执行器
     *
     * @param request 执行器注销请求
     */
    public void unregister(ExecutorRequest request) {
        checkParams(request);
        String executorAddress = StringUtils.trimToEmpty(request.getExecutorAddress());

        JobExecutor jobExecutor = findByCode(request);
        String executorAddresses = StringUtils.replace(StringUtils.trimToEmpty(jobExecutor.getExecutorAddresses()), " ", "");
        // 检查地址是否已注册
        if (!executorAddresses.contains(executorAddress)) {
            return;
        }
        // 移除执行器地址
        String[] addresses = executorAddresses.split(",");
        List<String> result = new ArrayList<>(addresses.length);
        for (String address : addresses) {
            if (StringUtils.contains(address, executorAddress)) {
                continue;
            }
            result.add(address);
        }
        jobExecutor.setExecutorAddresses(String.join(",", result));
        jobExecutorDao.saveAndFlush(jobExecutor);
    }

    /**
     * 检查请求参数
     *
     * @param request 执行器请求
     */
    private static void checkParams(ExecutorRequest request) {
        if (StringUtils.isBlank(request.getJobExecutorCode())) {
            throw new JobException("执行器编码不能空");
        }
        if (StringUtils.isBlank(request.getExecutorAddress())) {
            throw new JobException("执行器地址不能空");
        }
    }

    /**
     * 根据编码查找执行器
     *
     * @param request 执行器请求
     * @return 任务执行器对象
     */
    private JobExecutor findByCode(ExecutorRequest request) {
        JobExecutor jobExecutor = jobExecutorDao.findById(request.getJobExecutorCode()).orElse(null);
        if (jobExecutor == null) {
            throw new JobException("任务执行器不存在");
        }
        return jobExecutor;
    }
}
