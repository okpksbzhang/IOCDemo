package com.htc.iocdemo.annotataion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonBase {
    //以setOnclickListener为例
    String setCommonListener();//存储setOnclickListener名称，用于反射该方法
    Class setCommonObject();//存储View.OnClickListener类，用于反射setOnclickListener时传入参数的类型，并通过代理监听该接口
    String setCommonMethod();//消费的方法，在此暂时没用到

}
