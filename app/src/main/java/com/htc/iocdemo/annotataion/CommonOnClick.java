package com.htc.iocdemo.annotataion;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//作用在方法之上
@Retention(RetentionPolicy.RUNTIME)
//为该注解指定特定的三要素，相当于让CommonOnClick与OnClickListener事件进行绑定
@CommonBase(setCommonListener = "setOnClickListener",setCommonObject = View.OnClickListener.class,setCommonMethod = "onClick")
public @interface CommonOnClick {
    int[] value();//因为很多控件都有点击事件，所以用数组接收
}
