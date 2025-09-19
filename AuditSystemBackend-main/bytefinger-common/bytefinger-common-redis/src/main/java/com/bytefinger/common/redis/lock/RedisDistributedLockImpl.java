package com.bytefinger.common.redis.lock;

import com.bytefinger.common.core.utils.LockUtil;
import com.bytefinger.common.redis.lock.executor.LockExecutor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author patrick
 * @since 2020/10/23 11:06
 */
@Slf4j
@Component
@AllArgsConstructor
public class RedisDistributedLockImpl implements DistributedLock {

    private static final String PROCESS_ID = LockUtil.getLocalMAC() + LockUtil.getJvmPid();

    private final LockExecutor lockExecutor;

    /**
     * 加锁
     *
     * @param key      key
     * @param expire   锁的有效时间
     * @param waitTime 获取锁等待时间
     * @return 锁信息
     */
    @Override
    public RLock tryLock(String key, long expire, long waitTime) throws InterruptedException {
        Assert.isTrue(waitTime > 0, "tryTimeout must more than 0");
        long start = System.currentTimeMillis();
        int acquireCount = 0;
        String value = PROCESS_ID + Thread.currentThread().getId();
        while (System.currentTimeMillis() - start < waitTime) {
            boolean result = lockExecutor.acquire(key, value, expire);
            acquireCount++;
            if (result) {
                return new RLock(key, value, expire, waitTime, acquireCount);
            }
            Thread.sleep(100);
        }

        log.debug("{}.{} - key [{}] lock failed, try {} times ", Thread.currentThread().getStackTrace()[1].getClassName(), Thread.currentThread().getStackTrace()[1].getMethodName(), key, acquireCount);
        return null;
    }

    /**
     * 释放锁
     *
     * @param lock 锁信息
     * @return 是否成功
     */
    @Override
    public boolean release(RLock lock) {
        return lockExecutor.releaseLock(lock);
    }
}
