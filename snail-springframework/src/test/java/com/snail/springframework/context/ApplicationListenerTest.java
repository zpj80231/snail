package com.snail.springframework.context;

import com.snail.springframework.beans.factory.event.CustomEvent;
import com.snail.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author zhangpengjun
 * @date 2023/3/31
 */
public class ApplicationListenerTest {

    /**
     * 测试事件
     */
    @Test
    public void test_event() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-event.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, "13", "哟哟哟~"));
        applicationContext.registerShutdownHook();
    }

}
