package com.github.lock.core;

import com.github.lock.ILock;
import com.github.lock.LockInfo;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class ReadLock implements ILock {

    private RedissonClient redisson;

    private RReadWriteLock rLock;

    private LockInfo lockInfo;

    public ReadLock(RedissonClient redisson, LockInfo lockInfo) {
        this.redisson = redisson;
        this.lockInfo = lockInfo;
    }

    public boolean lock() {
        rLock = redisson.getReadWriteLock(lockInfo.getName());
        try {
            return rLock.readLock().tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        }catch (InterruptedException e) {
            return false;
        }
    }

    public void releaseLock() {
        if (rLock.readLock().isHeldByCurrentThread()){
            rLock.readLock().unlock();
        }
    }
}
