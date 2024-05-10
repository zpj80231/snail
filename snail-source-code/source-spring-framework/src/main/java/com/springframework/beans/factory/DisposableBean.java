package com.springframework.beans.factory;

import java.lang.reflect.InvocationTargetException;

/**
 * 如果 bean 实现了 DisposableBean 接口，容器会调用它的 destroy() 方法，完成 bean 的销毁。
 * 如果 bean 没有实现 DisposableBean 接口，容器会调用配置文件中指定的销毁方法（如果有）。
 *
 * @author zhangpengjun
 * @date 2023/3/24
 */
public interface DisposableBean {

    /**
     * 容器关闭时调用
     */
    void destroy() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

}
