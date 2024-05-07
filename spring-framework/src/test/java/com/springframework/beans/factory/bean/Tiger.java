package com.springframework.beans.factory.bean;

/**
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class Tiger implements Animal {

    private String name = "defaultName";

    @Override
    public String getAnimalName() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
