package com.yitiankeji.exception;

/**
 * 自定义运行时异常类JobException，用于处理作业相关的异常情况
 * 继承自RuntimeException，属于 unchecked exception
 */
public class JobException extends RuntimeException {

    /**
     * 构造方法，创建带有错误信息的JobException实例
     *
     * @param message 异常的详细信息，用于描述具体的错误原因
     */
    public JobException(String message) {
        super(message);
    }
}
