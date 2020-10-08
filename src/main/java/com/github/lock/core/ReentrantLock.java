package com.github.lock.core;

import com.github.lock.ILock;
import com.github.lock.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class ReentrantLock implements ILock {

    private RedissonClient redisson;

    private RLock rLock;

    private LockInfo lockInfo;

    public ReentrantLock(RedissonClient redisson, LockInfo lockInfo) {
        this.redisson = redisson;
        this.lockInfo = lockInfo;
    }

    public boolean lock() {
        rLock = redisson.getLock(lockInfo.getName());
        try {
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        }catch (InterruptedException e) {
            return false;
        }
    }

    public void releaseLock() {
        if (rLock.isHeldByCurrentThread()){
            rLock.unlock();
        }
    }
}
