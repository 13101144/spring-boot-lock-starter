package com.github.lock.autoconfigure;


import com.github.lock.DefaultRedisKeyGenerator;
import com.github.lock.LockInfoProvider;
import com.github.lock.aspect.LockAspectHandler;

import org.aspectj.lang.annotation.After;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(LockConfig.class)
@AutoConfigureBefore({RedisAutoConfiguration.class, RedissonAutoConfiguration.class})
@Import({LockAspectHandler.class})
public class LockAutoConfiguration {

    @Bean
    public LockInfoProvider lockInfoProvider(){
        return new LockInfoProvider();
    }

    @Bean
    public DefaultRedisKeyGenerator redisKeyGenerator(){
        return new DefaultRedisKeyGenerator();
    }

}
