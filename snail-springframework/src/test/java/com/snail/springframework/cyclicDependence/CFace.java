package com.snail.springframework.cyclicDependence;

import com.snail.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author zhangpengjun
 * @date 2023/9/19
 */
public class CFace implements FactoryBean<IFace> {

    @Override
    public IFace getObject() {
        return (IFace) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IFace.class}, ((proxy, method, args) ->
                "CFace proxy face method name: " + method.getName()
        ));
    }

    @Override
    public Class<?> getObjectType() {
        return IFace.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
