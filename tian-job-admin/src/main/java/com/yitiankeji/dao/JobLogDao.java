package com.yitiankeji.dao;

import com.yitiankeji.model.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * JobLogDao 数据访问层接口
 * 继承JpaRepository，提供JobLog实体的基本CRUD操作
 * 用于标注数据访问层，属于Spring持久层框架
 */
@Repository
public interface JobLogDao extends JpaRepository<JobLog, Long> {

    /**
     * 根据任务ID删除任务日志，删除条件为任务ID匹配指定值
     *
     * @param jobId 任务ID，用于删除对应任务的日志记录
     */
    @Query("delete from JobLog where jobId = ?1")
    void deleteByJobId(Long jobId);
}
