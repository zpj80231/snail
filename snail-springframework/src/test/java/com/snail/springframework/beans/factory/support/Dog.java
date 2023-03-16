package com.snail.springframework.beans.factory.support;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class Dog {

    private String name;

    private Cat cat;

    private static Map<String, Object> initMap = new HashMap<>();

    static {
        initMap.put("inner-dog1", "TomDog");
        initMap.put("inner-dog2", "JerryDog");
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
