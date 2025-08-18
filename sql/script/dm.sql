-- 达梦8 --

create sequence SEQ_TIAN_JOB     increment by 1 start with 1 nocache;
create sequence SEQ_TIAN_JOB_LOG increment by 1 start with 1 nocache;

/*==============================================================*/
/* Table: TIAN_JOB                                                */
/*==============================================================*/
create table TIAN_JOB
(
    JOB_ID            NUMBER(10)           not null,
    JOB_EXECUTOR_CODE VARCHAR2(200 CHAR),
    NAME              VARCHAR2(200 CHAR)   not null,
    CATEGORY          VARCHAR2(200 CHAR),
    CRON              VARCHAR2(200 CHAR)   not null,
    EXECUTOR_HANDLER  VARCHAR2(200 CHAR)   not null,
    EXECUTOR_PARAMS   CLOB,
    ROUTE_RULE        VARCHAR2(10 CHAR)    default '1' not null,
    BLOCK_STRATEGY    VARCHAR2(10 CHAR)    default '1',
    TIMEOUT           NUMBER(10),
    STATUS            VARCHAR2(10 CHAR)    default '0',
    RETRY_TIMES       NUMBER(10),
    CREATE_TIME       VARCHAR2(14 CHAR)    default to_char(sysdate,'yyyymmddhh24miss'),
    UPDATE_TIME       VARCHAR2(14 CHAR)    default to_char(sysdate,'yyyymmddhh24miss'),
    constraint PK_TIAN_JOB primary key (JOB_ID)
);

comment on table TIAN_JOB is '任务';
comment on column TIAN_JOB.JOB_ID is '任务ID';
comment on column TIAN_JOB.JOB_EXECUTOR_CODE is '执行器编码';
comment on column TIAN_JOB.NAME is '任务名称';
comment on column TIAN_JOB.CATEGORY is '任务分类';
comment on column TIAN_JOB.CRON is '调度表达式';
comment on column TIAN_JOB.EXECUTOR_HANDLER is '任务处理器';
comment on column TIAN_JOB.EXECUTOR_PARAMS is '任务参数';
comment on column TIAN_JOB.ROUTE_RULE is '路由规则(1第一个、2最后一个、3轮询、4随机、5分片广播)';
comment on column TIAN_JOB.BLOCK_STRATEGY is '阻塞处理策略(1单机串行、2丢弃后续调度、3覆盖之前调度)';
comment on column TIAN_JOB.TIMEOUT is '任务超时时间(毫秒)';
comment on column TIAN_JOB.RETRY_TIMES is '失败重试次数';
comment on column TIAN_JOB.STATUS is '启动状态(0未启动、1已启动)';
comment on column TIAN_JOB.CREATE_TIME is '创建时间';
comment on column TIAN_JOB.UPDATE_TIME is '修改时间';

/*==============================================================*/
/* Table: TIAN_JOB_EXECUTOR                                       */
/*==============================================================*/
create table TIAN_JOB_EXECUTOR
(
    JOB_EXECUTOR_CODE  VARCHAR2(200 CHAR)   not null,
    NAME               VARCHAR2(200 CHAR)   not null,
    REGISTER_TYPE      VARCHAR2(10 CHAR)    not null,
    EXECUTOR_ADDRESSES CLOB,
    CREATE_TIME        VARCHAR2(14 CHAR)    default to_char(sysdate,'yyyymmddhh24miss'),
    UPDATE_TIME        VARCHAR2(14 CHAR)    default to_char(sysdate,'yyyymmddhh24miss'),
    constraint PK_TIAN_JOB_EXECUTOR primary key (JOB_EXECUTOR_CODE)
);

comment on table TIAN_JOB_EXECUTOR is '执行器';
comment on column TIAN_JOB_EXECUTOR.JOB_EXECUTOR_CODE is '执行器编码';
comment on column TIAN_JOB_EXECUTOR.NAME is '执行器名称';
comment on column TIAN_JOB_EXECUTOR.REGISTER_TYPE is '注册方式(AUTO自动注册、MANUAL手动录入)';
comment on column TIAN_JOB_EXECUTOR.EXECUTOR_ADDRESSES is '执行器地址(逗号分隔)';
comment on column TIAN_JOB_EXECUTOR.CREATE_TIME is '创建时间';
comment on column TIAN_JOB_EXECUTOR.UPDATE_TIME is '修改时间';

/*==============================================================*/
/* Table: TIAN_JOB_LOG                                            */
/*==============================================================*/
create table TIAN_JOB_LOG
(
    JOB_LOG_ID                NUMBER(10)           not null,
    JOB_ID                    NUMBER(10),
    JOB_EXECUTOR_CODE         VARCHAR2(200 CHAR),
    EXECUTOR_ADDRESS          VARCHAR2(100 CHAR),
    EXECUTOR_HANDLER          VARCHAR2(200 CHAR)   not null,
    EXECUTOR_PARAMS           CLOB,
    EXECUTOR_FAIL_RETRY_COUNT NUMBER(10),
    ROUTE_RULE                VARCHAR2(10 CHAR)    default '1' not null,
    BLOCK_STRATEGY            VARCHAR2(10 CHAR)    default '1',
    TIMEOUT                   NUMBER(10),
    TRIGGER_TIME              VARCHAR2(14 CHAR),
    TRIGGER_CODE              VARCHAR2(10 CHAR),
    TRIGGER_MESSAGE           CLOB,
    constraint PK_TIAN_JOB_LOG primary key (JOB_LOG_ID)
);

comment on table TIAN_JOB_LOG is '任务执行日志';
comment on column TIAN_JOB_LOG.JOB_LOG_ID is '任务执行日志ID';
comment on column TIAN_JOB_LOG.JOB_ID is '任务ID';
comment on column TIAN_JOB_LOG.JOB_EXECUTOR_CODE is '执行器编码';
comment on column TIAN_JOB_LOG.EXECUTOR_ADDRESS is '执行器地址，本次执行的地址';
comment on column TIAN_JOB_LOG.EXECUTOR_HANDLER is '任务处理器';
comment on column TIAN_JOB_LOG.EXECUTOR_PARAMS is '任务参数';
comment on column TIAN_JOB_LOG.EXECUTOR_FAIL_RETRY_COUNT is '失败重试次数';
comment on column TIAN_JOB_LOG.ROUTE_RULE is '路由规则(1第一个、2最后一个、3轮询、4随机、5分片广播)';
comment on column TIAN_JOB_LOG.BLOCK_STRATEGY is '阻塞处理策略(1单机串行、2丢弃后续调度、3覆盖之前调度)';
comment on column TIAN_JOB_LOG.TIMEOUT is '任务超时时间(毫秒)';
comment on column TIAN_JOB_LOG.TRIGGER_TIME is '调度时间';
comment on column TIAN_JOB_LOG.TRIGGER_CODE is '调度结果';
comment on column TIAN_JOB_LOG.TRIGGER_MESSAGE is '调度日志';

/*==============================================================*/
/* Table: TIAN_JOB_LOCK                                           */
/*==============================================================*/
create table TIAN_JOB_LOCK
(
    JOB_LOCK_ID NUMBER(10)           not null,
    constraint PK_TIAN_JOB_LOCK primary key (JOB_LOCK_ID)
);

comment on table TIAN_JOB_LOCK is '任务锁';
comment on column TIAN_JOB_LOCK.JOB_LOCK_ID is '任务锁ID';

insert into TIAN_JOB_LOCK(JOB_LOCK_ID) values (1);