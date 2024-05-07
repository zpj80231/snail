package com.springframework.beans.factory;

/**
 * 实现该接口的 bean 可以获取 所属 ClassLoader 对象。
 *
 * @author zhangpengjun
 * @date 2023/3/28
 */
public interface BeanClassLoaderAware extends Aware {

    /**
     * 感知 所属 ClassLoader 对象
     *
     * @param classLoader 类装入器
     */
    void setBeanClassLoader(ClassLoader classLoader);

}
