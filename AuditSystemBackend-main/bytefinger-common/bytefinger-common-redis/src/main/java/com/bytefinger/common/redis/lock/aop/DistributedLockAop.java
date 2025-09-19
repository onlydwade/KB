package com.bytefinger.common.redis.lock.aop;

import com.bytefinger.common.redis.lock.DistributedLock;
import com.bytefinger.common.redis.lock.RLock;
import com.bytefinger.common.redis.lock.annotation.DLock;
import com.bytefinger.common.redis.lock.exception.LockException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

/**
 * @author patrick
 * @since 2020/10/23 11:25
 */
@Aspect
@Component
public class DistributedLockAop {

    private final LockKeyGenerator lockKeyGenerator = new LockKeyGenerator();

    private final DistributedLock distributedLock;

    public DistributedLockAop(DistributedLock distributedLock) {
        this.distributedLock = distributedLock;
    }

    @Around("@annotation(lock)")
    public Object before(ProceedingJoinPoint joinPoint, DLock lock) throws Throwable {
        Object target = joinPoint.getTarget();
        if (AopUtils.isAopProxy(target)) {
            return joinPoint.proceed();
        }
        RLock rLock = null;
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String keyName = lockKeyGenerator.getKeyName(signature.getMethod(), joinPoint.getArgs(), lock);

            rLock = distributedLock.tryLock(keyName, lock.expire(), lock.waitTime());
            if (rLock != null) {
                return joinPoint.proceed();
            }

            if (lock.exception()) {
                throw new LockException();
            }

            return null;
        } finally {
            if (rLock != null) {
                distributedLock.release(rLock);
            }
        }
    }
}
