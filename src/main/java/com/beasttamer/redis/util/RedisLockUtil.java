package com.beasttamer.redis.util;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @author a small asshole
 * @version 1.0
 * @description redis分布式锁
 * @date in 14:57 2020/1/13
 * @since 1.0
 */
public class RedisLockUtil {
    public static final Long RELEASE_SUCCESS = 1L;

    /**
     * 常识获取分布式锁
     * @param jedis redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识//不一定要是请求id，只要是能标识出是谁加锁的，方便后面释放锁
     * @param expireTime 过期时间
     * @return
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey, requestId, "NX", "PX", expireTime);
        return "OK".equals(result);
    }

    /**
     *释放锁
     * @param jedis
     * @param lockKey 锁
     * @param requestId 请求标识//不一定要是请求id，只要是能标识出是谁加锁的，方便后面释放锁
     * @return
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId){
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

}
