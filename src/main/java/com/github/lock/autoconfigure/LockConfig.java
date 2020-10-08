package com.github.lock.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = LockConfig.PREFIX)
public class LockConfig {
    public static final String PREFIX = "spring.lock";

    //lock
    private long waitTime = 60;
    private long leaseTime = 60;

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
}
