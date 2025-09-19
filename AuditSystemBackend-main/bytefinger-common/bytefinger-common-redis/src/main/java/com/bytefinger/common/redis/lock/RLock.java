package com.bytefinger.common.redis.lock;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author janyd
 * @since 2020/10/23 11:01
 */
@Data
@AllArgsConstructor
public class RLock implements Serializable {

    /**
     * 锁名称
     */
    private String lockKey;

    /**
     * 锁值
     */
    private String lockValue;

    /**
     * 过期时间
     */
    private Long expire;

    /**
     * 获取锁超时时间
     */
    private Long acquireTimeout;

    /**
     * 获取锁次数
     */
    private int acquireCount = 0;
}
