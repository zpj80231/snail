package com.snail.springframework.beans.factory.bean;

import com.snail.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author zhangpengjun
 * @date 2023/3/28
 */
public class AnimalFactoryBean implements FactoryBean<Animal> {

    /**
     * 要代理的对象
     */
    private Animal animal;

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public Animal getObject() {
        System.out.println("-- into FactoryBean getObject() --");
        InvocationHandler handler = (proxy, method, args) -> {
            System.out.println("-- into FactoryBean JdkProxy method:[" + method.getName() + "] before --");
            Object result = method.invoke(animal, args);
            System.out.println("-- into FactoryBean JdkProxy method:[" + method.getName() + "] after --");
            return result;
        };
        return (Animal) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{Animal.class}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return Animal.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
