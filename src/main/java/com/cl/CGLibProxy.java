package com.cl;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB能代理类和接口
 * 需要实现 MethodInterceptor 方法
 * Created By chenli
 * ON 17/11/24
 */
public class CGLibProxy {
    /**
     * 这里以代理 『类』 为例
     * 代理的类
     */
    static class SayHello {
        /**
         * 方法
         */
        public void sayHello(){
            System.out.println("说Hello了....");
        }
    }

    /**
     * CGLIB代理需要实现 MethodInterceptor 接口
     */
    static class MyIntercepter<T> implements MethodInterceptor {

        public T target;

        /**
         * 通过构造方法赋值需要代理的类
         * @param target
         */
        public MyIntercepter(T target){
            this.target = target;
        }

        /**
         * 创建代理对象
         */
        public T getProxy(){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(this.target.getClass());
            // 回调方法
            enhancer.setCallback(this);
            // 创建代理对象
            return (T) enhancer.create();
        }
        /**
         * 实现
         * @param obj
         * @param method
         * @param args
         * @param methodProxy
         * @return
         * @throws Throwable
         */
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            System.out.println("说Hello前....");
            Object result = methodProxy.invokeSuper(obj,args);
            System.out.println("说Hello后....");
            return result;
        }
    }

    /**
     * 测试代理
     * @param args
     */
    public static void main(String[] args) {
        //代理的类
        SayHello sayHello = new SayHello();
        //传入代理的类
        MyIntercepter<SayHello> myIntercepter = new MyIntercepter(sayHello);
        //获取代理对象
        SayHello sayHelloProxy = myIntercepter.getProxy();
        //调用方法
        sayHelloProxy.sayHello();
    }
}
