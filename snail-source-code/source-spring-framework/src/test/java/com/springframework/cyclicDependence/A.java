package com.springframework.cyclicDependence;

/**
 * @author zhangpengjun
 * @date 2023/9/19
 */
public class A {

    private B b;

    public String print() {
        return "A print b: " + b;
    }

}
