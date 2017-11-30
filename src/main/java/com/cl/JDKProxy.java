package com.cl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk动态代理只能代理接口
 * 需要实现InvocationHandler接口
 * Created By chenli
 * ON 17/11/24
 */
public class JDKProxy {
    /**
     * 需要代理的接口
     */
    interface SayHello {
        void sayHello();
    }

    /**
     * 实现hello方法
     */
    static class SayHelloImpl implements  SayHello {

        public void sayHello() {
            System.out.println("说Hello了......");
        }
    }
    //需求需要在说Sayhello之前做一些操作
    //需要用到代理。代理类需要实现 InvocationHandler 接口
    static class MyInvocationHander<T> implements InvocationHandler {
        /**
         * 目标代理对象
         */
        private T target;

        /**
         * 无参构造函数赋值
         * @param target
         */
        public MyInvocationHander(T target){
            this.target = target;
        }
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //方法调用前
            System.out.println("说Hello之前...");
            //对目标对象利用反射调用方法
            //如何得到目标对象？需要重写无参构造方法
            Object result = method.invoke(this.target,args);
            //方法调用后
            System.out.println("说Hello之后...");
            return result;
        }
        //获取代理类
        public T getProxy(){
            T proxy = (T) Proxy.newProxyInstance(this.target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
            return proxy;
        }
    }

    /**
     * 调用示例
     * @param args
     */
    public static void main(String[] args) {
        //代理对象
        SayHello sayHello = new SayHelloImpl();
        //通过构造函数传入代理对象
        MyInvocationHander<SayHello> myInvocationHander = new MyInvocationHander(sayHello);
        //获得代理对象
        SayHello  proxy = myInvocationHander.getProxy();
        //调用方法
        proxy.sayHello();
    }
}
