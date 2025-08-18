package com.yitiankeji.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/** 执行器 **/
@Data
@Entity
@Table(name = "TIAN_JOB_EXECUTOR")
public class JobExecutor {
    /** 执行器编码 **/
    @Id
    private String jobExecutorCode;
    /** 执行器名称 **/
    private String name;
    /** 注册方式(AUTO自动注册、MANUAL手动录入)，默认为AUTO **/
    private String registerType;
    /** 执行器地址(逗号分隔) **/
    private String executorAddresses;
    /** 创建时间 **/
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /** 修改时间 **/
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public List<String> getExecutorAddressList() {
        if (StringUtils.isBlank(executorAddresses)) {
            return Collections.emptyList();
        }
        List<String> address = new ArrayList<>();
        executorAddresses = StringUtils.trimToEmpty(executorAddresses);
        String[] urls = executorAddresses.split("[,;]");
        for (String url : urls) {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            address.add(url);
        }
        return address;
    }
}
