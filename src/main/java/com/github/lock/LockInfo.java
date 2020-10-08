package com.github.lock;

import com.github.lock.enums.LockType;

public class LockInfo {

    private String name;

    private long waitTime;

    private long leaseTime;

    private LockType lockType;

    private String[] keys;

    public LockInfo(String name, long waitTime, long leaseTime, LockType lockType, String[] keys) {
        this.name = name;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
        this.lockType = lockType;
        this.keys = keys;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public LockType getLockType() {
        return lockType;
    }

    public void setLockType(LockType lockType) {
        this.lockType = lockType;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }
}
