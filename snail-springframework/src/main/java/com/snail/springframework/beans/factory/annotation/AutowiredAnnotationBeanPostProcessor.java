package com.snail.springframework.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.PropertyValues;
import com.snail.springframework.beans.factory.BeanFactory;
import com.snail.springframework.beans.factory.BeanFactoryAware;
import com.snail.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.snail.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.snail.springframework.util.ClassUtils;

import java.lang.reflect.Field;

/**
 * Spring 注解驱动的核心组件之一，处理的Bean 的依赖注入，处理的相关的注解有：@Autowired @Value
 *
 * @author zhangpengjun
 * @date 2023/9/15
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;

        // 处理 @Value 注解
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Value valueAnnotation = field.getDeclaredAnnotation(Value.class);
            if (valueAnnotation != null) {
                String value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue(value);
                BeanUtil.setFieldValue(bean, field.getName(), value);
            }
        }

        // 处理 @Autowired 注解
        for (Field field : declaredFields) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (autowiredAnnotation != null) {
                Class<?> fieldType = field.getType();
                Object dependentBean = null;
                Qualifier qualifierAnnotation = field.getDeclaredAnnotation(Qualifier.class);
                // 优先按照 @Qualifier 注入，再按照类型注入
                if (qualifierAnnotation != null) {
                    String dependentBeanName = qualifierAnnotation.value();
                    dependentBean = beanFactory.getBean(dependentBeanName, fieldType);
                } else {
                    dependentBean = beanFactory.getBean(fieldType);
                }
                BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
            }
        }

        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        return true;
    }
}
