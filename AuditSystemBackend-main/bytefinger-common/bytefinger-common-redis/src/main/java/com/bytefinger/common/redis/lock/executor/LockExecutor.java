package com.bytefinger.common.redis.lock.executor;

import com.bytefinger.common.redis.lock.RLock;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

/**
 * @author patrick
 * @since 2020/10/23 11:05
 */
@Component
@AllArgsConstructor
public class LockExecutor {

    private static final RedisScript<String> SCRIPT_LOCK = new DefaultRedisScript<>("return redis.call('set',KEYS[1],ARGV[1],'NX','PX',ARGV[2])", String.class);
    private static final RedisScript<String> SCRIPT_UNLOCK = new DefaultRedisScript<>("if redis.call('get',KEYS[1]) == ARGV[1] then return tostring(redis.call('del', KEYS[1])==1) else return 'false' end", String.class);
    private static final String LOCK_SUCCESS = "OK";

    public final RedisTemplate<String, Object> redisTemplate;

    public boolean acquire(String lockKey, String lockValue, long acquireExpire) {
        Object lockResult = redisTemplate.execute(SCRIPT_LOCK,
                redisTemplate.getStringSerializer(),
                redisTemplate.getStringSerializer(),
                Collections.singletonList(lockKey),
                lockValue, String.valueOf(acquireExpire));
        return LOCK_SUCCESS.equals(lockResult);
    }


    public boolean releaseLock(RLock lock) {
        Object releaseResult = redisTemplate.execute(SCRIPT_UNLOCK,
                redisTemplate.getStringSerializer(),
                redisTemplate.getStringSerializer(),
                Collections.singletonList(lock.getLockKey()),
                lock.getLockValue());
        return Boolean.parseBoolean(Objects.requireNonNull(releaseResult).toString());
    }
}
