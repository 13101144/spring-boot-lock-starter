package com.github.lock.aspect;

import com.github.lock.ILock;
import com.github.lock.LockContext;
import com.github.lock.LockInfo;
import com.github.lock.LockInfoProvider;
import com.github.lock.annotation.Lock;
import com.github.lock.exception.LockFailException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(0)
public class LockAspectHandler {

    private static final Logger logger = LoggerFactory.getLogger(LockAspectHandler.class);

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private LockInfoProvider lockInfoProvider;

    private ThreadLocal<ILock> currentThreadLock = new ThreadLocal();
    private ThreadLocal<Boolean> currentThreadLockReleaseLock = new ThreadLocal();

    @Around("@annotation(lock)")
    public Object aroundLock(ProceedingJoinPoint joinPoint, Lock lock) throws Throwable  {
        currentThreadLockReleaseLock.set(false);
        LockInfo lockInfo = lockInfoProvider.getLockInfo(joinPoint, lock);
        LockContext lockContext = new LockContext(redisson, lockInfo);

        boolean isLock = lockContext.lock();
        if (!isLock) {
            throw new LockFailException("get lock fail!!!");
        }
        this.currentThreadLock.set(lockContext.getLock());
        this.currentThreadLockReleaseLock.set(isLock);
        return joinPoint.proceed();

    }

    @AfterReturning("@annotation(lock)")
    public void afterReturning(Lock lock) {
        if ((Boolean)currentThreadLockReleaseLock.get()) {
            ((ILock)currentThreadLock.get()).releaseLock();
            currentThreadLock.remove();
        }

    }

    @AfterThrowing("@annotation(lock)")
    public void afterThrowing(Lock lock) {
        if ((Boolean)currentThreadLockReleaseLock.get()) {
            ((ILock)currentThreadLock.get()).releaseLock();
            currentThreadLock.remove();
        }

    }
}
