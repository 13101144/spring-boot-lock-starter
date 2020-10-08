package com.github.lock;

import com.github.lock.annotation.Lock;
import org.aspectj.lang.JoinPoint;

public interface RedisKeyGenerator {

    /**
     * 生成redis key
     * @return
     */
    String generateRedisKey(JoinPoint joinPoint, Lock lock);

}
