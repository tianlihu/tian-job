package com.yitiankeji.dao;

import com.yitiankeji.model.JobExecutor;
import com.yitiankeji.query.JobExecutorQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JobExecutorDao 数据接口，继承JpaRepository，提供JobExecutor实体的数据库访问能力
 * 用于标注数据访问层组件，表示这是一个DAO层组件
 */
@Repository
public interface JobExecutorDao extends JpaRepository<JobExecutor, String> {

    /**
     * 分页查询JobExecutor列表
     * 支持按jobExecutorCode和name进行模糊查询
     *
     * @param query    查询条件对象，包含jobExecutorCode和name等查询参数
     * @param pageable 分页参数，包含页码、每页大小等信息
     * @return 返回分页查询结果，包含JobExecutor列表及分页信息
     */
    @Query("select e from JobExecutor e " +
            "where (:#{#query.jobExecutorCode} is null or e.jobExecutorCode like %:#{#query.jobExecutorCode}%) " +
            "  and (:#{#query.name} is null or e.name like %:#{#query.name}%) ")
    Page<JobExecutor> page(@Param("query") JobExecutorQuery query, Pageable pageable);

    /**
     * 查询所有JobExecutor列表
     * 不带任何查询条件，返回全部数据
     *
     * @return 返回所有JobExecutor的列表
     */
    @Query("select e from JobExecutor e")
    List<JobExecutor> list();
}
