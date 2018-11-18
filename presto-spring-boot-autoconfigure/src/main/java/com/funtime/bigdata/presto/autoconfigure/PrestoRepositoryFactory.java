package com.funtime.bigdata.presto.autoconfigure;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PrestoRepositoryFactory<T> implements InitializingBean, FactoryBean<T> {
    private String innerClassName;

    public void setInnerClassName(String innerClassName) {
        this.innerClassName = innerClassName;
    }

    public T getObject() throws Exception {
        Class innerClass = Class.forName(innerClassName);
        if (innerClass.isInterface()) {
            return (T) InterfaceProxy.newInstance(innerClass);
        } else {
            throw new BeanInitializationException("aaaa");
        }
    }

    public Class<?> getObjectType() {
        if (innerClassName == null)
            return null;
        try {
            return Class.forName(innerClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {

    }

    public static class InterfaceProxy implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("ObjectProxy execute:" + method.getName());
            return method.invoke(proxy, args);
        }

        public static <T> T newInstance(Class<T> innerInterface) {
            ClassLoader classLoader = innerInterface.getClassLoader();
            Class[] interfaces = new Class[] { innerInterface };
            InterfaceProxy proxy = new InterfaceProxy();
            return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
        }
    }


}
