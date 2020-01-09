package com.htc.iocdemo.annotataion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//在方法之上
@Retention(RetentionPolicy.RUNTIME)
public @interface MOnclick {
    int[] value();
}
