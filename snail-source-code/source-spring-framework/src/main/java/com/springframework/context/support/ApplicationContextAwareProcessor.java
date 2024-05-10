package com.springframework.context.support;

import com.springframework.beans.factory.config.BeanPostProcessor;
import com.springframework.context.ApplicationContext;
import com.springframework.context.ApplicationContextAware;

/**
 * 应用程序上下文Aware比较特殊，因为在容器直接创建 Bean 的时候没有上下文的存在，
 * 所以只能交由上下文refresh()的时候，将 ApplicationContextAware 包装作为一个 Bean 的后置处理器添加到容器中，
 * 然后在Bean创建的时候，应用Bean前置处理，将上下文设置到Bean中取。
 * <p>
 * 所以：ApplicationContextAware 底层是 通过 BeanPostProcessor 实现的。
 *
 * @author zhangpengjun
 * @date 2023/3/28
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
