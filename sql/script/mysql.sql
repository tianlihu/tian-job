-- MySQL 8.0.26 --

-- ----------------------------
-- 任务表
-- ----------------------------
CREATE TABLE `TIAN_JOB`
(
    `JOB_ID`            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '任务ID',
    `JOB_EXECUTOR_CODE` VARCHAR(200) COMMENT '执行器编码',
    `NAME`              VARCHAR(200) NOT NULL COMMENT '任务名称',
    `CATEGORY`          VARCHAR(200) COMMENT '任务分类',
    `CRON`              VARCHAR(200) NOT NULL COMMENT '调度表达式',
    `EXECUTOR_HANDLER`  VARCHAR(200) NOT NULL COMMENT '任务处理器',
    `EXECUTOR_PARAMS`   LONGTEXT COMMENT '任务参数',
    `ROUTE_RULE`        VARCHAR(10)  NOT NULL DEFAULT '1' COMMENT '路由规则(1第一个、2最后一个、3轮询、4随机、5分片广播)',
    `BLOCK_STRATEGY`    VARCHAR(10)           DEFAULT '1' COMMENT '阻塞处理策略(1单机串行、2丢弃后续调度、3覆盖之前调度)',
    `TIMEOUT`           BIGINT COMMENT '任务超时时间(毫秒)',
    `STATUS`            VARCHAR(10)           DEFAULT '0' COMMENT '启动状态(0未启动、1已启动)',
    `RETRY_TIMES`       INT COMMENT '失败重试次数',
    `CREATE_TIME`       DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_TIME`       DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE = InnoDB COMMENT ='任务';

-- ----------------------------
-- 执行器表
-- ----------------------------
CREATE TABLE `TIAN_JOB_EXECUTOR`
(
    `JOB_EXECUTOR_CODE`  VARCHAR(200) PRIMARY KEY COMMENT '执行器编码',
    `NAME`               VARCHAR(200) NOT NULL COMMENT '执行器名称',
    `REGISTER_TYPE`      VARCHAR(10)  NOT NULL COMMENT '注册方式(AUTO自动注册、MANUAL手动录入)',
    `EXECUTOR_ADDRESSES` LONGTEXT COMMENT '执行器地址(逗号分隔)',
    `CREATE_TIME`        DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_TIME`        DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE = InnoDB COMMENT ='执行器';

-- ----------------------------
-- 任务日志表
-- ----------------------------
CREATE TABLE `TIAN_JOB_LOG`
(
    `JOB_LOG_ID`                BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '任务执行日志ID',
    `JOB_ID`                    BIGINT COMMENT '任务ID',
    `JOB_EXECUTOR_CODE`         VARCHAR(200) COMMENT '执行器编码',
    `EXECUTOR_ADDRESS`          VARCHAR(100) COMMENT '执行器地址，本次执行的地址',
    `EXECUTOR_HANDLER`          VARCHAR(200) NOT NULL COMMENT '任务处理器',
    `EXECUTOR_PARAMS`           LONGTEXT COMMENT '任务参数',
    `EXECUTOR_FAIL_RETRY_COUNT` INT COMMENT '失败重试次数',
    `ROUTE_RULE`                VARCHAR(10)  NOT NULL DEFAULT '1' COMMENT '路由规则(1第一个、2最后一个、3轮询、4随机、5分片广播)',
    `BLOCK_STRATEGY`            VARCHAR(10)           DEFAULT '1' COMMENT '阻塞处理策略(1单机串行、2丢弃后续调度、3覆盖之前调度)',
    `TIMEOUT`                   BIGINT COMMENT '任务超时时间(毫秒)',
    `TRIGGER_TIME`              DATETIME COMMENT '调度时间',
    `TRIGGER_CODE`              VARCHAR(10) COMMENT '调度结果',
    `TRIGGER_MESSAGE`           LONGTEXT COMMENT '调度日志'
) ENGINE = InnoDB COMMENT ='任务执行日志';

-- ----------------------------
-- 任务锁表
-- ----------------------------
CREATE TABLE `TIAN_JOB_LOCK`
(
    `JOB_LOCK_ID` INT PRIMARY KEY COMMENT '任务锁ID'
) ENGINE = InnoDB COMMENT ='任务锁';

INSERT INTO `TIAN_JOB_LOCK`(`JOB_LOCK_ID`) VALUES (1);