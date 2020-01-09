package com.htc.iocdemo.annotataion;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@CommonBase(setCommonListener = "setOnLongClickListener",setCommonObject = View.OnLongClickListener.class,setCommonMethod = "onLongClick")
public @interface CommonOnLongClick {
    int[] value();;
}
