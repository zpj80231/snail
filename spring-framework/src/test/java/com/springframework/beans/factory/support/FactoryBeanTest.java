package com.springframework.beans.factory.support;

import com.springframework.beans.factory.bean.Animal;
import com.springframework.beans.factory.bean.AnimalFactoryBean;
import com.springframework.beans.factory.bean.Cat;
import com.springframework.beans.factory.bean.Mouse;
import com.springframework.beans.factory.bean.Tiger;
import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author zhangpengjun
 * @date 2023/3/28
 */
public class FactoryBeanTest {

    /**
     * bean scope 测试
     */
    @Test
    public void bean_scope_test() {
        // 1. 利用 xml上下文 加载Bean
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-scope.xml");
        applicationContext.registerShutdownHook();
        // 2. 获取bean
        Mouse mouse1 = (Mouse) applicationContext.getBean("mouse");
        Mouse mouse2 = (Mouse) applicationContext.getBean("mouse");
        System.err.println("mouse1：" + mouse1.hashCode());
        System.err.println("mouse2：" + mouse2.hashCode());
        System.err.println("Mouse 是否单例：" + (mouse1 == mouse2));

        System.err.println();

        Cat cat1 = (Cat) applicationContext.getBean("cat");
        Cat cat2 = (Cat) applicationContext.getBean("cat");
        System.err.println("cat1：" + cat1.hashCode());
        System.err.println("cat2：" + cat1.hashCode());
        System.err.println("Cat 是否单例：" + (cat1 == cat2));
    }

    /**
     * FactoryBean 测试
     */
    @Test
    public void factoryBean_test() {
        // 1. 利用 xml上下文 加载Bean
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-factorybean.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取原始 FactoryBean 对象
        AnimalFactoryBean animalFactoryBean = (AnimalFactoryBean) applicationContext.getBean("&animalFactoryBean");
        System.err.println("获取原始 FactoryBean 对象: " + animalFactoryBean);
        Tiger tiger = new Tiger();
        tiger.setName("我是小老虎ai~");
        animalFactoryBean.setAnimal(tiger);

        // 3. 获取真正的 bean，并调用它的方法，
        // 当然利用代理可以在调用它的方法前后做一些其他操作
        Animal proxyAnimal = (Animal) applicationContext.getBean("animalFactoryBean");
        System.err.println("获取真实 FactoryBean 目标 bean 对象: " + proxyAnimal);
        System.err.println(proxyAnimal.getAnimalName());
    }

}
