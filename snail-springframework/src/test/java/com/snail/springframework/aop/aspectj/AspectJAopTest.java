package com.snail.springframework.aop.aspectj;

import com.snail.springframework.aop.AdvisedSupport;
import com.snail.springframework.aop.TargetSource;
import com.snail.springframework.aop.framework.Cglib2AopProxy;
import com.snail.springframework.aop.framework.JdkDynamicAopProxy;
import com.snail.springframework.beans.factory.bean.Animal;
import com.snail.springframework.beans.factory.bean.Cat;
import com.snail.springframework.beans.factory.bean.Tiger;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author zhangpengjun
 * @date 2023/4/6
 */
public class AspectJAopTest {

    @Test
    public void aspectJExpressionPointcutTest() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut =
                new AspectJExpressionPointcut("execution(* com.snail.springframework.beans.factory.bean.Cat.*(..))");
        Class<Cat> clazz = Cat.class;
        Method method = clazz.getDeclaredMethod("toString");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method, clazz));
    }

    @Test
    public void aopTest() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut =
                new AspectJExpressionPointcut("execution(* com.snail.springframework.beans.factory.bean.Animal.*(..))");
        Tiger tiger = new Tiger();
        tiger.setName("[11 hu~]");
        System.out.println("tiger：" + tiger.getAnimalName());
        System.out.println("------------------");

        AdvisedSupport advised = new AdvisedSupport();
        advised.setMethodMatcher(pointcut);
        advised.setTargetSource(new TargetSource(tiger));
        advised.setMethodInterceptor(new TigerInterceptor());

        Animal jdkProxy = (Animal) new JdkDynamicAopProxy(advised).getProxy();
        tiger.setName("[12 hu~ jdkProxy]");
        System.out.println("jdk aop：" + jdkProxy.getAnimalName());
        System.out.println("------------------");
        Animal cglibProxy = (Animal) new Cglib2AopProxy(advised).getProxy();
        tiger.setName("[13 hu~ cglibProxy]");
        System.out.println("cglib aop：" + cglibProxy.getAnimalName());
    }

}
