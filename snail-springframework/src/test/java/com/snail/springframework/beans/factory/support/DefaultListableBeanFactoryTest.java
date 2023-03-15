package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.factory.config.BeanDefinition;
import org.junit.Test;

/**
 * @author zhangpengjun
 * @date 2023/3/15
 */
public class DefaultListableBeanFactoryTest {

    @Test
    public void testDefaultListableBeanFactoryGetBean() {
        // DefaultListableBeanFactory 的用法
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 1. 注册Bean的能力：将一个普通对象转换为 BeanDefinition，并注册进容器中
        BeanDefinition beanDefinition = new BeanDefinition(Cat.class);
        beanFactory.registerBeanDefinition("cat", beanDefinition);
        // 2. 获取Bean的能力：从容器中获取指定 Bean，第一次获取会示例化并缓存
        Cat cat = (Cat) beanFactory.getBean("cat");
        cat.printName();
        // 3. 第二次会直接从缓存中获取
        Cat cat_cache = (Cat) beanFactory.getBean("cat");
        cat_cache.printName();
    }

    @Test
    public void testDefaultListableBeanFactoryGetBeanWithConstructor() {
        // DefaultListableBeanFactory 的用法
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 1. 注册Bean的能力：将一个普通对象转换为 BeanDefinition，并注册进容器中
        BeanDefinition beanDefinition = new BeanDefinition(Cat.class);
        beanFactory.registerBeanDefinition("cat", beanDefinition);
        // 2. 获取Bean的能力：从容器中获取指定 Bean，第一次获取会示例化并缓存
        Cat cat = (Cat) beanFactory.getBean("cat", "Cat -> Constructor");
        cat.printName();
        // 3. 第二次会直接从缓存中获取
        Cat cat_cache = (Cat) beanFactory.getBean("cat");
        cat_cache.printName();
    }

}
