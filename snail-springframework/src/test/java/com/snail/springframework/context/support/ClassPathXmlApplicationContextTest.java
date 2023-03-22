package com.snail.springframework.context.support;

import com.snail.springframework.beans.factory.support.CatBeanPostProcessor;
import com.snail.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.snail.springframework.beans.factory.support.Dog;
import com.snail.springframework.beans.factory.support.DogBeanFactoryPostProcessor;
import com.snail.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.junit.Test;

/**
 * @author zhangpengjun
 * @date 2023/3/21
 */
public class ClassPathXmlApplicationContextTest {

    @Test
    public void test_beanFactoryAndPostProcessor() {
        // 1. 初始化 Bean 工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2. 使用加载器加载xml配置文件，初始化 BeanDefinition
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");
        // 3. 添加 BeanDefinition 扩展点，（这里的扩展点是手动调用的）
        // 在 Bean 实例化前，BeanDefinition 初始化后
        new DogBeanFactoryPostProcessor().postProcessBeanFactory(beanFactory);
        // 4. 添加 Bean 扩展点
        // Bean 初始化前后回调相应方法
        CatBeanPostProcessor catBeanPostProcessor = new CatBeanPostProcessor();
        beanFactory.addBeanPostProcessor(catBeanPostProcessor);
        // 5. 获取bean
        Dog dog = (Dog) beanFactory.getBean("dog");
        System.out.println();
        dog.printName();
    }

}
