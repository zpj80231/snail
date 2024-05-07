package com.springframework.beans.factory.bean;

import com.springframework.beans.factory.DisposableBean;
import com.springframework.beans.factory.InitializingBean;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class Dog implements InitializingBean, DisposableBean {

    private String name;

    private Cat cat;

    private static Map<String, Object> initMap = new HashMap<>();

    static {
        initMap.put("inner-dog1", "TomDog");
        initMap.put("inner-dog2", "JerryDog");
    }

    @Override
    public void afterPropertiedSet() {
        System.out.println("-- Dog afterPropertiedSet method by interface --");
    }

    @Override
    public void destroy() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("-- Dog destroy method by interface --");
    }

    public void printName() {
        System.out.println(this.name);
        System.out.println(this.cat);
        System.out.println(initMap);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", cat=" + cat +
                '}';
    }
}
