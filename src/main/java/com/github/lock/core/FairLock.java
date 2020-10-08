package com.github.lock.core;

import com.github.lock.ILock;
import com.github.lock.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 公平锁
 */
public class FairLock implements ILock {

    private RedissonClient redisson;

    private RLock rLock;

    private LockInfo lockInfo;

    public FairLock(RedissonClient redisson, LockInfo lockInfo) {
        this.redisson = redisson;
        this.lockInfo = lockInfo;
    }

    public boolean lock() {
        rLock = redisson.getFairLock(lockInfo.getName());
        try {
            long start = System.currentTimeMillis();
            rLock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
            long end = System.currentTimeMillis();
            System.out.println(start+" - "+end+" = "+ (end-start));
        }catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    public void releaseLock() {
        if (rLock.isHeldByCurrentThread()) {
            rLock.unlock();
        }
    }
}
