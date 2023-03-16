package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.PropertyValue;
import com.snail.springframework.beans.PropertyValues;
import com.snail.springframework.beans.factory.config.BeanDefinition;
import com.snail.springframework.beans.factory.config.BeanReference;
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

    @Test
    public void testDefaultListableBeanFactoryGetBeanWithApplyPropertyValues() {
        // DefaultListableBeanFactory 的用法
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 1. 注册Bean的能力：将一个普通对象（和属性）转换为 BeanDefinition，并注册进容器中
        // 注册一个 cat
        PropertyValues catPropertyValues = new PropertyValues();
        catPropertyValues.addPropertyValue(new PropertyValue("name", "TomCat"));
        BeanDefinition catBeanDefinition = new BeanDefinition(Cat.class, catPropertyValues);
        beanFactory.registerBeanDefinition("cat", catBeanDefinition);
        // 注册一个 dog，dog 依赖 cat
        PropertyValues dogPropertyValues = new PropertyValues();
        dogPropertyValues.addPropertyValue(new PropertyValue("name", "JjDog"));
        dogPropertyValues.addPropertyValue(new PropertyValue("cat", new BeanReference("cat")));
        BeanDefinition dogBeanDefinition = new BeanDefinition(Dog.class, dogPropertyValues);
        beanFactory.registerBeanDefinition("dog", dogBeanDefinition);

        // 2. 获取Bean的能力：从容器中获取指定 Bean，第一次获取会示例化并缓存
        Dog dog = (Dog) beanFactory.getBean("dog");
        dog.printName();
    }

}
