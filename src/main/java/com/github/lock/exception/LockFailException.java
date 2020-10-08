package com.github.lock.exception;

public class LockFailException extends RuntimeException {

    public LockFailException(String msg) {
        super(msg);
    }
}
