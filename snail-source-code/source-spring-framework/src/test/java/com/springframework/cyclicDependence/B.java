package com.springframework.cyclicDependence;

/**
 * @author zhangpengjun
 * @date 2023/9/19
 */
public class B {

    private A a;
    private IFace c;

    public String print() {
        return "B print a: " + a + "\n\t\tB print Face c: " + c.getFace();
    }

}
