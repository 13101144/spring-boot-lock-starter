package com.github.lock.annotation;

import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LockKey {

    String value() default "";

}
