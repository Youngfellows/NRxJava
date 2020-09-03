package com.necho.nrxjava.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义限额注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)//保留到JVM运行时
@Target(ElementType.METHOD)//作用于方法
public @interface BankTransferMoney {
    /**
     * 定义注解的属性(也就相当于接口的方法)
     */
    double maxMoney() default 1000;
}
