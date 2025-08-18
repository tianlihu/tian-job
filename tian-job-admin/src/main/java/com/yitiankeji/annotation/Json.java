package com.yitiankeji.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 定义一个注解，用于标记需要被序列化为JSON的方法 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Json {

    /** 定义一个名为value的属性，类型为boolean，默认值为true */
    boolean value() default true;
}
