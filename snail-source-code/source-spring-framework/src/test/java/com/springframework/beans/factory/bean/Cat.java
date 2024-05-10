package com.springframework.beans.factory.bean;

public class Cat {

    private String name;

    public Cat() {

    }

    public Cat(String name) {
        this.name = name;
    }

    public void init() {
        System.out.println("-- Cat init method by xml --");
    }

    public void destroy() {
        System.out.println("-- Cat destroy method by xml --");
    }

    public void printName() {
        if (name != null) {
            System.out.println(name);
        } else {
            System.out.println("My name is TomCat");
        }
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                '}';
    }
}
