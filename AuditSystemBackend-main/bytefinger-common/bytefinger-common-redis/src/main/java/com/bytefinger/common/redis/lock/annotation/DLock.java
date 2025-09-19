package com.bytefinger.common.redis.lock.annotation;

import java.lang.annotation.*;

/**
 * 分布式锁注解
 *
 * @author patrick
 * @since 2020/10/23 11:15
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface DLock {

    String name() default "";

    /**
     * KEY 默认包名+方法名
     */
    String[] keys() default "";

    /**
     * 过期时间 单位：毫秒
     * <pre>
     *     过期时间一定是要长于业务的执行时间.
     * </pre>
     */
    long expire() default 30000;

    /**
     * 获取锁超时时间 单位：毫秒
     * <pre>
     *     结合业务,建议该时间不宜设置过长,特别在并发高的情况下.
     * </pre>
     */
    long waitTime() default 3000;

    /**
     * 是否需要抛出异常
     */
    boolean exception() default false;
}
