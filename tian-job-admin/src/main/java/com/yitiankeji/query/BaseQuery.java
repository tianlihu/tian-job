package com.yitiankeji.query;

import lombok.Data;

@Data
public class BaseQuery {

    /** 当前页码 **/
    private Integer page = 1;
    /** 每页记录数量 **/
    private Integer pageSize = 20;
}
