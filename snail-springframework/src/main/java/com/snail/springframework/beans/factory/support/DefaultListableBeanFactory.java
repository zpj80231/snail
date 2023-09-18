package com.snail.springframework.beans.factory.support;

import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.snail.springframework.beans.factory.config.BeanDefinition;
import com.snail.springframework.beans.factory.config.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 核心类，具有 注册 BeanDefinition 的能力，获取 Bean 的能力。
 *
 * @author zhangpengjun
 * @date 2023/3/15
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No bean: " + beanName + " is defined");
        }
        return beanDefinition;
    }

    @Override
    public void preInstantiateSingletons() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.isSingleton()) {
                getBean(beanName);
            }
        }
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        HashMap<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class<?> beanClass = beanDefinition.getBeanClass();
            if (type.isAssignableFrom(beanClass)) {
                result.put(beanName, (T) getBean(beanName));
            }
        });
        return result;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        List<String> beanNames = new ArrayList<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class<?> beanClass = beanDefinition.getBeanClass();
            if (requiredType.isAssignableFrom(beanClass)) {
                beanNames.add(beanName);
            }
        });
        if (1 == beanNames.size()) {
            return getBean(beanNames.get(0), requiredType);
        }
        throw new BeansException(requiredType + "expected single bean but found " + beanNames.size() + ": " + beanNames);
    }

}
