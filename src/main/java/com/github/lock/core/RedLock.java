package com.github.lock.core;

import com.github.lock.ILock;
import com.github.lock.LockInfo;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class RedLock implements ILock {

    private RedissonClient redisson;

    private RedissonRedLock rLock;

    private LockInfo lockInfo;

    public RedLock(RedissonClient redisson, LockInfo lockInfo) {
        this.redisson = redisson;
        this.lockInfo = lockInfo;
    }

    public boolean lock() {
        RLock[] rLocks = new RLock[lockInfo.getKeys().length];

        for (int i = 0; i < lockInfo.getKeys().length; i++) {
            rLocks[i] = redisson.getLock(lockInfo.getKeys()[i]);
        }

        rLock = new RedissonRedLock(rLocks);
        try {
            return rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void releaseLock() {
        rLock.unlock();

    }
}
