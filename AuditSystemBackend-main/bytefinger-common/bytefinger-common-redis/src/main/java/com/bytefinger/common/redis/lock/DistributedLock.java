package com.bytefinger.common.redis.lock;

/**
 * 分布式锁
 *
 * @author janyd
 * @since 2020/10/23 10:37
 */
public interface DistributedLock {

    /**
     * 加锁
     *
     * @param key      key
     * @param expire   锁的有效时间
     * @param waitTime 获取锁等待时间
     * @return 锁信息
     */
    RLock tryLock(String key, long expire, long waitTime) throws InterruptedException;

    /**
     * 释放锁
     *
     * @param lock 锁信息
     * @return 是否成功
     */
    boolean release(RLock lock);
}
