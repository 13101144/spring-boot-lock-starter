package com.github.lock.core;

import com.github.lock.ILock;
import com.github.lock.LockInfo;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class WriteLock implements ILock {

    private RedissonClient redisson;

    private RReadWriteLock rLock;

    private LockInfo lockInfo;

    public WriteLock(RedissonClient redisson, LockInfo lockInfo) {
        this.redisson = redisson;
        this.lockInfo = lockInfo;
    }

    public boolean lock() {
        rLock = redisson.getReadWriteLock(lockInfo.getName());
        try {
            return rLock.writeLock().tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        }catch (InterruptedException e) {
            return false;
        }
    }

    public void releaseLock() {
        if (rLock.writeLock().isHeldByCurrentThread()){
            rLock.writeLock().unlock();
        }
    }

}
