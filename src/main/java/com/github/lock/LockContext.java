package com.github.lock;


import com.github.lock.core.*;
import org.redisson.api.RedissonClient;

public class LockContext {

    private RedissonClient redisson;

    private ILock lock;

    public LockContext(RedissonClient redisson, LockInfo lockInfo){

        switch (lockInfo.getLockType()) {
            case FAIR:
                lock = new FairLock(redisson, lockInfo);
                break;
            case MULTI:
                lock = new MultiLock(redisson, lockInfo);
                break;
            case READ:
                lock = new ReadLock(redisson, lockInfo);
                break;
            case WRITE:
                lock = new WriteLock(redisson, lockInfo);
                break;
            case REENTRANT:
                lock = new ReentrantLock(redisson, lockInfo);
                break;
            case RED:
                lock = new ReadLock(redisson, lockInfo);
                break;
            default:
                lock = new FairLock(redisson, lockInfo);
        }
    }

    public boolean lock(){
        return lock.lock();
    }

    public void releaseLock(){
        lock.releaseLock();
    }

    public ILock getLock() {
        return lock;
    }
}
