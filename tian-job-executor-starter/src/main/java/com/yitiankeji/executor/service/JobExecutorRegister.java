package com.yitiankeji.executor.service;

import com.yitiankeji.executor.JobExecutor;
import com.yitiankeji.executor.exception.JobException;
import com.yitiankeji.executor.response.HttpResult;
import com.yitiankeji.executor.utils.HttpUtils;
import com.yitiankeji.executor.utils.IpUtils;
import com.yitiankeji.executor.utils.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;

/**
 * 任务执行器注册服务类
 * 负责任务执行器在管理平台的注册和注销
 */
@Slf4j
@Component
public class JobExecutorRegister {

    @Resource
    private JobExecutor jobExecutor;
    @Value("${server.port}")
    private Integer serverPort;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${tian.job.executor.ip:}")
    private String executorIp;

    /**
     * 初始化方法，在Bean创建完成后执行
     * 用于向管理平台注册当前任务执行器
     */
    @PostConstruct
    public void register() {
        // 构建注册URL
        String registerUrl = getAdminAddress(jobExecutor) + "/jobExecutor/register";
        // 获取任务执行器编码
        String jobExecutorCode = jobExecutor.getJobExecutorCode();
        // 获取执行器地址
        String executorAddress = getExecutorAddress();
        // 发送注册请求
        HttpResult result = HttpUtils.post(registerUrl, null, Maps.of("jobExecutorCode", jobExecutorCode, "executorAddress", executorAddress));
        // 处理注册结果
        if (result.isError()) {
            log.error("注册任务调度管理平台出错：{}", result.getErrorMessage());
            throw new JobException("注册任务调度管理平台出错：" + result.getErrorMessage());
        }
    }

    /**
     * 销毁方法，在Bean销毁前执行
     * 用于从管理平台注销当前任务执行器
     */
    @PreDestroy
    public void destroy() {
        // 构建注销URL
        String unregisterUrl = getAdminAddress(jobExecutor) + "/jobExecutor/unregister";
        // 获取任务执行器编码
        String jobExecutorCode = jobExecutor.getJobExecutorCode();
        // 获取执行器地址
        String executorAddress = getExecutorAddress();
        // 发送注销请求
        HttpResult result = HttpUtils.post(unregisterUrl, null, Maps.of("jobExecutorCode", jobExecutorCode, "executorAddress", executorAddress));
        // 处理注销结果
        if (result.isError()) {
            log.error("注销任务调度管理平台出错：{}", result.getErrorMessage());
        } else {
            log.error("注销任务调度管理平台成功：{}", unregisterUrl);
        }
    }

    /**
     * 获取管理平台地址
     *
     * @param executor 任务执行器实例
     * @return 管理平台地址
     * @throws JobException 当管理平台地址为空时抛出异常
     */
    private static String getAdminAddress(JobExecutor executor) {
        // 获取管理平台地址列表
        String adminAddresses = executor.getAdminAddresses();
        if (StringUtils.isBlank(adminAddresses)) {
            throw new JobException("任务调度管理平台的地址不能为空");
        }
        adminAddresses = StringUtils.trimToEmpty(adminAddresses);
        // 移除末尾的斜杠
        if (adminAddresses.endsWith("/")) {
            adminAddresses = adminAddresses.substring(0, adminAddresses.length() - 1);
        }
        return adminAddresses;
    }

    /**
     * 获取执行器地址
     *
     * @return 执行器地址
     * @throws JobException 当无法获取有效IP地址时抛出异常
     */
    private String getExecutorAddress() {
        // 如果配置了执行器IP，直接使用
        if (StringUtils.isNotBlank(executorIp)) {
            return "http://" + executorIp + ":" + serverPort + contextPath;
        }
        // 获取管理平台IP列表
        List<String> adminIps = IpUtils.getHosts(jobExecutor.getAdminAddresses());
        // 获取与执行器在同一网段的IP列表
        List<String> executorIps = IpUtils.sameSubnetIPs(adminIps, IpUtils.allIps());
        log.info("平台IP地址列表：{}", adminIps);
        log.info("执行器IP地址：{}", executorIps);

        // 检查是否有有效的执行器IP
        if (executorIps.isEmpty()) {
            throw new JobException("未能取得有效的调度器IP地址，调度平台和调度器可能不在同一网段");
        }
        // 使用第一个IP作为执行器地址
        String executorIp = executorIps.get(0);
        return "http://" + executorIp + ":" + serverPort + contextPath;
    }
}
