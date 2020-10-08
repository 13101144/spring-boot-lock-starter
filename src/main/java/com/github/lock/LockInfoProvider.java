package com.github.lock;

import com.github.lock.annotation.Lock;
import com.github.lock.annotation.LockKey;
import com.github.lock.autoconfigure.LockConfig;
import com.github.lock.enums.LockType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LockInfoProvider {

    @Autowired
    private LockConfig lockConfig;

    private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private ExpressionParser parser = new SpelExpressionParser();

    public LockInfo getLockInfo(ProceedingJoinPoint joinPoint, Lock lock) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        List<String> keyList = this.getKeyList(joinPoint, lock);
        String lockName = this.getLockName(lock.name(), signature, keyList);
        long waitTime = this.getLockWaitTime(lock);
        long leaseTime = this.getLockLeaseTime(lock);
        LockType type= lock.lockType();
        return new LockInfo(lockName, waitTime, leaseTime, type, keyList.toArray(new String[keyList.size()]));

    }

    private long getLockWaitTime(Lock lock) {
        return lock.waitTime() == Long.MIN_VALUE ? this.lockConfig.getWaitTime() : lock.waitTime();
    }

    private long getLockLeaseTime(Lock lock) {
        return lock.leaseTime() == Long.MIN_VALUE ? this.lockConfig.getLeaseTime() : lock.leaseTime();
    }

    private List<String> getKeyList(JoinPoint joinPoint, Lock lock) {
        List<String> keyList = new ArrayList();
        Method method = this.getMethod(joinPoint);
        List<String> definitionKeys = this.getSpelDefinitionKey(lock.keys(), method, joinPoint.getArgs());
        keyList.addAll(definitionKeys);
        List<String> parameterKeys = this.getParameterKey(method.getParameters(), joinPoint.getArgs());
        keyList.addAll(parameterKeys);
        return keyList;
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return method;
    }

    private List<String> getSpelDefinitionKey(String[] definitionKeys, Method method, Object[] parameterValues) {
        List<String> definitionKeyList = new ArrayList();
        String[] var5 = definitionKeys;
        int var6 = definitionKeys.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String definitionKey = var5[var7];
            if (definitionKey != null && !definitionKey.isEmpty()) {
                EvaluationContext context = new MethodBasedEvaluationContext((Object)null, method, parameterValues, this.nameDiscoverer);
                Object value = this.parser.parseExpression(definitionKey).getValue(context);
                if (value != null) {
                    definitionKeyList.add(value.toString());
                }
            }
        }

        return definitionKeyList;
    }

    private List<String> getParameterKey(Parameter[] parameters, Object[] parameterValues) {
        List<String> parameterKey = new ArrayList();

        for(int i = 0; i < parameters.length; ++i) {
            if (parameters[i].getAnnotation(LockKey.class) != null) {
                LockKey keyAnnotation = (LockKey)parameters[i].getAnnotation(LockKey.class);
                if (keyAnnotation.value().isEmpty()) {
                    parameterKey.add(parameterValues[i].toString());
                } else {
                    StandardEvaluationContext context = new StandardEvaluationContext(parameterValues[i]);
                    Object value = this.parser.parseExpression(keyAnnotation.value()).getValue(context);
                    if (value != null) {
                        parameterKey.add(value.toString());
                    }
                }
            }
        }

        return parameterKey;
    }

    private String getLockName(String annotationName, MethodSignature signature, List<String> keyList) {
        return annotationName.isEmpty() ? String.format("%s.%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName(), StringUtils.collectionToDelimitedString(keyList, "", "-", "")) : annotationName;
    }


}
