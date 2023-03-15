package com.snail.springframework.beans.factory.support;

public class Cat {

    private String name;

    public Cat() {

    }

    public Cat(String name) {
        this.name = name;
    }

    public void printName() {
        if (name != null) {
            System.out.println(name);
        } else {
            System.out.println("My name is TomCat");
        }
    }
}
