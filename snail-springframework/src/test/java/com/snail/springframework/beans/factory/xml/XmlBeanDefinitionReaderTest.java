package com.snail.springframework.beans.factory.xml;

import com.snail.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.snail.springframework.beans.factory.support.Dog;
import org.junit.Test;

/**
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class XmlBeanDefinitionReaderTest {

    @Test
    public void test_xml() {
        // 核心容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 读取 xml 解析为 Bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");
        // 获取指定 Bean
        Dog dog = (Dog) beanFactory.getBean("dog");
        dog.printName();
    }
}
