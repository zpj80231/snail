package com.springframework.util;

/**
 * @author zhangpengjun
 * @date 2023/9/15
 */
public class ClassUtils {

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }

}
