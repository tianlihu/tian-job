package com.yitiankeji.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.yitiankeji"})
public class ExecutorDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExecutorDemoApplication.class, args);
    }
}