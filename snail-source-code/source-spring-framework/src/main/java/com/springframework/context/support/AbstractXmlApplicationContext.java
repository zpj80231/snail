package com.springframework.context.support;

import com.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author zhangpengjun
 * @date 2023/3/21
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationiContext {

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getConfigLocations();

}
