package com.htc.iocdemo;

import android.util.Log;
import android.view.View;

import com.htc.iocdemo.annotataion.CommonBase;
import com.htc.iocdemo.annotataion.MBindView;
import com.htc.iocdemo.annotataion.MContentView;
import com.htc.iocdemo.annotataion.MOnclick;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectTool {
    public static final String TAG = InjectTool.class.getSimpleName();

    public static void inject(Object object){
        //实现注入布局
        injectContentView(object);
        //实现注入控件
        injectBindView(object);
//        injectOnClick(object);
        //实现注解事件
        injectCommonListener(object);
    }

    private static void injectOnClick(final Object object) {
        final Class<?> clazz = object.getClass();

        //获取所有方法并遍历
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (final Method declaredMethod : declaredMethods) {
            //得到方法上的注解
            MOnclick mOnclick = declaredMethod.getAnnotation(MOnclick.class);
            if (mOnclick==null){
                Log.d(TAG, "injectOnClick: 没有找到相应的点击事件"+declaredMethod.getName());
                continue;
            }
            //获取到所有的id并遍历
            int[] ids = mOnclick.value();
            for (final int id : ids) {
                //todo 通过findViewById来实例化对象
                try {
                    //首先获取findViewById方法
                    Method findViewById = clazz.getMethod("findViewById", int.class);
                    //执行方法，获取到需要操作的对象
                    Object invokeObject = findViewById.invoke(object, id);
                    //我们知道这个对象一定继承自View，因为onclick事件在View里所以直接转成View类型
                    View viewObject = (View) invokeObject;
                    viewObject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //执行方法
                            try {
                                declaredMethod.invoke(object,id);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     *  tv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
    });

     1、执行此方法，首先需要找到对象tv
     2、获取到三要素
     3、通过反射执行setOnClickListener方法
     * @param object
     */
    private static void injectCommonListener(final Object object) {
        Class<?> clazz = object.getClass();
        //获取所有方法，并遍历    getDeclaredMethods可获取到当前对象的private protected等，而  clazz.getMethod()只能获取到public的，单其包括父类的public
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (final Method declaredMethod : declaredMethods) {
            //获取方法上的所有注解并遍历
            Annotation[] annotations =
                    declaredMethod.getAnnotations();
            for (Annotation annotation : annotations) {
                //判断该注解上有无CommonBase注解
                CommonBase commonBase = annotation.annotationType().getAnnotation(CommonBase.class);
                if (commonBase==null){
                    //如果没有注解则直接跳过
                    continue;
                }
                //已找到CommonBase注解，获取三要素
                String commonListener = commonBase.setCommonListener();
                Class commonObject = commonBase.setCommonObject();
                String commonMethod = commonBase.setCommonMethod();
                //获取注解值

                try {
                    //通过反射获取注解annotation中的value值并遍历
                    Method valueMethod = annotation.getClass().getDeclaredMethod("value");
                    int[] values= (int[]) valueMethod.invoke(annotation);
                    for ( int value : values) {
                        //获取到id后通过反射获取findViewById方法
                        Method findViewById = clazz.getMethod("findViewById", int.class);
                        //执行findViewById
                        Object invokeObject = findViewById.invoke(object, value);
                        //通过反射获取到invokeObject对象中的commonListener方法
                        Method realCommonMethod = invokeObject.getClass().getMethod(commonListener, commonObject);
                        final int ff = value;
                        //采用动态代理监听commonObject接口的执行，因为当用户执行commonObject事件时，一定会执行commonObject接口，而动态代理刚好可以监听到
                        Object proxy = Proxy.newProxyInstance(commonObject.getClassLoader(), new Class[]{commonObject}, new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                args[0] = ff;
                                //当监听到用户点击了事件后，去执行我们自己的declareMethod方法
                                return declaredMethod.invoke(object,args);
                            }
                        });
                        //将代理赋值给我们的realCommonMethod对象
                        realCommonMethod.invoke(invokeObject,proxy);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void injectBindView(Object object) {
        Class<?> activity = object.getClass();

        //遍历所有字段
        Field[] declaredFields = activity.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            //获取字段上的注解
            MBindView mBindView = declaredField.getAnnotation(MBindView.class);
            if (mBindView==null){
                Log.d(TAG, "injectBindView: 没找到该注解");
                continue;
            }
            int value = mBindView.value();
            if (value==-1){
                Log.d(TAG, "injectBindView: 请设置绑定id");
                return;
            }
            try {
                Method findViewById = activity.getMethod("findViewById", int.class);
                Object invoke = findViewById.invoke(object, value);//执行findViewById方法（findViewById(xxx)）
                declaredField.set(object,invoke);//将已经执行的findViewById复制给拥有注解的declareField对象，即（btn = findViewById(xxx)的复制操作）
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param object 该对象即传入的MainActivity
     */
    private static void injectContentView(Object object) {
        //获取对象的类
        Class<?> activity = object.getClass();
        //获取类上的MContentView注解
        MContentView contentView = activity.getAnnotation(MContentView.class);
        //如果contentView等于空则代表没有该注解，便直接返回
        if (contentView==null){
            Log.d(TAG, "injectContentView: 没找到该注解");
            return;
        }
        //如果由该注解，则获得该注解上的layoutId
        int value = contentView.value();
        try {
            //通过反射，反射除类activity里的setContentView方法并执行该方法
            Method method = activity.getMethod("setContentView",//该参数是反射的方法名称，因为我们要执行setContentView()，所以反射setContentView
                    int.class);//反射方法中需要携带的参数的类型

            method.invoke(object,// 第一个参数是指该方法在哪个类中执行，因为我们是在object对象中执行的setContentView方法，所以传入object
                    value);//这个参数是指执行setContentView方法所需要的参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
