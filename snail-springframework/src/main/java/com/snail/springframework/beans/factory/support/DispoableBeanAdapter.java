package com.snail.springframework.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.factory.DisposableBean;
import com.snail.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * DisposableBeanAdapter 实现了 DisposableBean 接口，并将其委托给一个实际的 Bean 实例来执行销毁操作。
 *
 * @author zhangpengjun
 * @date 2023/3/24
 */
public class DispoableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private final String destroyMethodName;

    public DispoableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 1. 实现接口的方式调用
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
        // 2. xml 方式反射调用初始化方法
        if (StrUtil.isNotBlank(destroyMethodName) && !(bean instanceof DisposableBean)) {
            Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
            if (destroyMethod == null) {
                throw new BeansException("Could not found an init method name: " + destroyMethodName);
            }
            destroyMethod.invoke(bean);
        }
    }

}
