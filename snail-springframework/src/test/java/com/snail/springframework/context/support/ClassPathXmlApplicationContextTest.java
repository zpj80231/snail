package com.snail.springframework.context.support;

import cn.hutool.json.JSONUtil;
import com.snail.springframework.beans.factory.support.CatBeanPostProcessor;
import com.snail.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.snail.springframework.beans.factory.support.Dog;
import com.snail.springframework.beans.factory.support.DogBeanFactoryPostProcessor;
import com.snail.springframework.beans.factory.support.Mouse;
import com.snail.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.junit.Test;

/**
 * @author zhangpengjun
 * @date 2023/3/21
 */
public class ClassPathXmlApplicationContextTest {

    /**
     * 传统 容器 测试
     */
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

    /**
     * 简化容器操作，xml上下文 + BeanPostProcessor，测试
     */
    @Test
    public void test_xml_context() {
        // 1. 利用 xml上下文 加载Bean
        // 上下文的高级实现极大的简化了或融合了上述的 1-4 步操作
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        // 2. 获取bean
        Dog dog = (Dog) applicationContext.getBean("dog");

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println();
        System.out.println(JSONUtil.toJsonStr(beanDefinitionNames));
        System.out.println();
        dog.printName();
    }

    /**
     * xml上下文中，加入 Bean 的初始化和销毁，测试
     */
    @Test
    public void test_xml_context_close() {
        // 1. 利用 xml上下文 加载Bean
        // 上下文的高级实现极大的简化了或融合了上述的 1-4 步操作
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        // * 可以在任何时段，提前注册 JVM 关闭钩子，用来确保 Spring 容器在 JVM 关闭之前正确地关闭并释放所有资源。
        applicationContext.registerShutdownHook();
        // 2. 获取bean
        Dog dog = (Dog) applicationContext.getBean("dog");

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println();
        System.out.println(JSONUtil.toJsonStr(beanDefinitionNames));
        System.out.println();
        dog.printName();
        // * close 和 registerShutdownHook 比，比较暴力，直接手动调用关闭
        // applicationContext.close();
    }

    /**
     * xml上下文中，加入 Bean 的 Aware（感知能力），测试
     */
    @Test
    public void test_xml_context_aware() {
        // 1. 利用 xml上下文 加载Bean
        // 上下文的高级实现极大的简化了或融合了上述的 1-4 步操作
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-aware.xml");
        // 可以在任何时段，提前注册 JVM 关闭钩子，用来确保 Spring 容器在 JVM 关闭之前正确地关闭并释放所有资源。
        applicationContext.registerShutdownHook();
        // 2. 获取bean
        Mouse mouse = (Mouse) applicationContext.getBean("mouse");

        System.out.println("\n" + mouse);
        // * close 和 registerShutdownHook 比，比较暴力，直接手动调用关闭
        // applicationContext.close();
    }

}
