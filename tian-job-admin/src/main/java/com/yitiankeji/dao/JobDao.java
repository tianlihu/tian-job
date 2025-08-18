package com.yitiankeji.dao;

import com.yitiankeji.model.Job;
import com.yitiankeji.query.JobQuery;
import com.yitiankeji.view.JobView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JobDao接口，继承自JpaRepository，提供Job实体的数据库访问操作
 * 通过@Repository注解标记为数据访问层组件
 */
@Repository
public interface JobDao extends JpaRepository<Job, Long> {

    /**
     * 分页查询Job信息
     *
     * @param query    查询条件对象，包含jobExecutorCode、status、jobName、jobHandler等查询条件
     * @param pageable 分页参数
     * @return 返回分页后的JobView结果
     * 使用JPQL语句，支持动态查询条件，当查询条件为null时不参与查询
     */
    @Query(value =
            "select new com.yitiankeji.view.JobView(" +
                    "   j.jobId, j.jobExecutorCode, e.name, j.name, j.category, j.cron, " +
                    "   j.executorHandler, j.executorParams, j.routeRule, j.blockStrategy, " +
                    "   j.timeout, j.retryTimes, j.status, j.createTime, j.updateTime) " +
                    "from Job j left join JobExecutor e on j.jobExecutorCode = e.jobExecutorCode " +
                    "where (:#{#query.jobExecutorCode} is null or j.jobExecutorCode like %:#{#query.jobExecutorCode}%) " +
                    "  and (:#{#query.status}          is null or j.status = :#{#query.status}) " +
                    "  and (:#{#query.jobName}         is null or j.name like %:#{#query.jobName}%) " +
                    "  and (:#{#query.jobHandler}      is null or j.executorHandler = :#{#query.jobHandler})")
    Page<JobView> page(@Param("query") JobQuery query, Pageable pageable);

    /**
     * 查询所有状态为RUNNING的Job
     *
     * @return 返回状态为RUNNING的Job列表
     */
    @Query("select job from Job job where job.status='RUNNING'")
    List<Job> selectRunningJobs();

    /**
     * 检查指定jobExecutorCode是否存在Job
     *
     * @param jobExecutorCode 执行器编码
     * @return 如果存在Job返回true，否则返回false
     */
    @Query("select count(1) from Job job where job.jobExecutorCode=:jobExecutorCode")
    boolean hasJobs(String jobExecutorCode);
}
