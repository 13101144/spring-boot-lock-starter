package com.github.lock;

public interface ILock {

    boolean lock();

    void releaseLock();

}
