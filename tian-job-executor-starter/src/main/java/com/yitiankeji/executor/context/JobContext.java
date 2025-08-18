package com.yitiankeji.executor.context;

import com.yitiankeji.executor.annotation.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class JobContext implements SmartInitializingSingleton {

    @Resource
    private ApplicationContext applicationContext;
    private final Map<String, MethodJobHandler> jobHandlerMethodMap = new ConcurrentHashMap<>();

    public void afterSingletonsInstantiated() {
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean;
            Lazy onBean = applicationContext.findAnnotationOnBean(beanDefinitionName, Lazy.class);
            if (onBean != null) {
                log.debug("扫描 @Job 注解, 跳过@Lazy懒加载Bean:{}", beanDefinitionName);
                continue;
            } else {
                bean = applicationContext.getBean(beanDefinitionName);
            }

            Map<Method, Job> annotatedMethods = null;
            try {
                annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(), (MethodIntrospector.MetadataLookup<Job>) method -> AnnotatedElementUtils.findMergedAnnotation(method, Job.class));
            } catch (Throwable e) {
                log.error("解析添加了 @Job 注解的执行任务方法失败，Bean名称[{}]", beanDefinitionName, e);
            }
            if (annotatedMethods == null || annotatedMethods.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, Job> methodJobEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodJobEntry.getKey();
                Job job = methodJobEntry.getValue();
                // 注册执行任务方法
                registerJobHandler(job, bean, executeMethod);
            }
        }
    }

    public MethodJobHandler getJobHandler(String jobHandlerName) {
        return jobHandlerMethodMap.get(jobHandlerName);
    }

    private void registerJobHandler(Job job, Object bean, Method executeMethod) {
        MethodJobHandler jobHandler = new MethodJobHandler(bean, executeMethod);
        jobHandlerMethodMap.put(job.value(), jobHandler);
        log.info(">>>>>>>>>>> 注册执行任务方法成功, 名称:{}, 执行器:{}", job.value(), jobHandler);
    }
}
