package com.yitiankeji.dao;

import com.yitiankeji.model.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface JobLockDao extends JpaRepository<JobLog, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)   // 生成  FOR UPDATE
    @Query("select j from JobLog j where j.jobLogId = 1")
    void lock();
}
