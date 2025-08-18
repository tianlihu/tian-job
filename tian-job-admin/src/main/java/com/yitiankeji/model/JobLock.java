package com.yitiankeji.model;

import lombok.Data;

import javax.persistence.*;

/** 任务锁 **/
@Data
@Entity
@Table(name = "TIAN_JOB_LOCK")
public class JobLock {
    /** 任务锁ID **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobLockId;
}
