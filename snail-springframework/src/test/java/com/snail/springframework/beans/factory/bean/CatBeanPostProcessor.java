package com.snail.springframework.beans.factory.bean;

import com.snail.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author zhangpengjun
 * @date 2023/3/21
 */
public class CatBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("beanName：" + beanName + " 进入到 Bean 扩展点 BeanPostProcessor -> postProcessBeforeInitialization");
        if ("cat".equals(beanName)) {
            Cat cat = (Cat) bean;
            return new Cat("postProcessBeforeInitialization name cat");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("beanName：" + beanName + " 进入到 Bean 扩展点 BeanPostProcessor -> postProcessAfterInitialization");
        return bean;
    }

}
