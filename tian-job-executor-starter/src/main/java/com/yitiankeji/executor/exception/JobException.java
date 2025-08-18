package com.yitiankeji.executor.exception;

public class JobException extends RuntimeException {

    public JobException(String message) {
        super(message);
    }

    public JobException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
