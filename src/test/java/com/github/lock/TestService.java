package com.github.lock;

import com.github.lock.annotation.Lock;
import com.github.lock.annotation.LockKey;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Lock(waitTime = 10, leaseTime = 60, keys = {"#param"})
    public String getValue(String param) throws Exception {
        //  if ("sleep".equals(param)) {//线程休眠或者断点阻塞，达到一直占用锁的测试效果
        Thread.sleep(1000*3);
        //}
        return "success";
    }

    @Lock(keys = {"#userId"})
    public String getValue(String userId,@LockKey Integer id)throws Exception{
        Thread.sleep(60*1000);
        return "success";
    }

    @Lock(keys = {"#user.name","#user.id"})
    public String getValue(User user)throws Exception{
        Thread.sleep(60*1000);
        return "success";
    }
}
