package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.PropertyValue;
import com.snail.springframework.beans.PropertyValues;
import com.snail.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.snail.springframework.beans.factory.config.BeanDefinition;
import com.snail.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * @author zhangpengjun
 * @date 2023/3/21
 */
public class DogBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        System.out.println("进入到 BeanDefinition 扩展点 BeanFactoryPostProcessor -> postProcessBeanFactory");
        String beanName = "dog";
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        PropertyValue propertyValue = propertyValues.getPropertyValue("name");
        String oldValue = String.valueOf(propertyValue.getValue());
        String updateValue = beanName + "-BeanDefinition-updated";
        propertyValue.setValue(updateValue);
        System.out.println("beanName：" + beanName + " 属性：name，原先值：" + oldValue + " 修改后值：" + updateValue);
        System.out.println();
    }

}
