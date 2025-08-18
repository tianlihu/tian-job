package com.yitiankeji.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class JobExecutorQuery extends BaseQuery {

    /** 执行器编码 **/
    private String jobExecutorCode;
    /** 执行器名称 **/
    private String name;
}
