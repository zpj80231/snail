package com.springframework.context.support;

import com.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author zhangpengjun
 * @date 2023/3/21
 */
public abstract class AbstractRefreshableApplicationiContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() {
        // 创建容器
        beanFactory = createBeanFactory();
        // 加载Bean定义，留给子类实现
        loadBeanDefinitions(beanFactory);
    }

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
