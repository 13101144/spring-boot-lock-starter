package com.github.lock.annotation;

import com.github.lock.enums.LockType;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

    /**
     * 锁的名字
     * @return
     */
    String name() default "";

    /**
     * 锁的类型，默认是可重入锁
     * @return
     */
    LockType lockType() default LockType.REENTRANT;

    /**
     * 尝试加锁，最多等待时间
     * @return
     */
    long waitTime() default Long.MIN_VALUE;

    /**
     * 加锁时间，超过时间自动解锁
     * @return
     */
    long leaseTime() default Long.MIN_VALUE;

    /**
     * 业务key
     * @return
     */
    String[] keys() default {};


}
