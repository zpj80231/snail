package com.springframework.cyclicDependence;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author zhangpengjun
 * @date 2023/9/13
 */
public class SpringCyclicDependenceTest {

    /**
     * 测试三级缓存循环引用
     */
    @Test
    public void test_cyclic_dependence() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-cyclic-dependence.xml");
        A a = applicationContext.getBean(A.class);
        System.out.println("测试结果：" + a.print());
        B b = applicationContext.getBean(B.class);
        System.out.println("测试结果：" + b.print());
        // 测试结果：A print b: com.springframework.cyclicDependence.B$$EnhancerByCGLIB$$d03a45b9@37374a5e
        // 测试结果：B print a: com.springframework.cyclicDependence.A$$EnhancerByCGLIB$$c473690e@4671e53b
        // 		B print Face c: proxy face method name: getFace
    }

}
