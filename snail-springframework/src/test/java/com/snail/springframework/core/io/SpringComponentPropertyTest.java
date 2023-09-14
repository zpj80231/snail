package com.snail.springframework.core.io;

import com.snail.springframework.beans.factory.bean.RabbitComponent;
import com.snail.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author zhangpengjun
 * @date 2023/9/13
 */
public class SpringComponentPropertyTest {

    /**
     * 测试从指定包下扫描 @Component 注解
     * RabbitComponent 的属性没有赋值，返回结果应该是 null
     */
    @Test
    public void test_scan_component() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-component-scan.xml");
        RabbitComponent rabbitComponent = (RabbitComponent) applicationContext.getBean("rabbitComponent");
        System.out.println("测试结果：" + rabbitComponent.getAnimalName());
        // 测试结果：null
    }

    /**
     * 测试从配置文件加载 bean，并且完成 bean 类的 ${value} 属性替换
     * RabbitComponent 的属性从配置文件 ${nameFromPropertyPlaceholder} 完成替换
     */
    @Test
    public void test_scan_properties_replace() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-component-property.xml");
        RabbitComponent rabbitComponent = (RabbitComponent) applicationContext.getBean("rabbitComponent");
        System.out.println("测试结果：" + rabbitComponent.getAnimalName());
        // 测试结果：rabbit001
    }

}
