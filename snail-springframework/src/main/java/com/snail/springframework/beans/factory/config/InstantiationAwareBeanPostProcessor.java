package com.snail.springframework.beans.factory.config;

import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.PropertyValues;

/**
 * InstantiationAwareBeanPostProcessor 接口的主要作用在于：目标对象的实例化过程中需要处理的事情，包括实例化对象的前后过程以及实例的属性设置
 *
 * @author zhangpengjun
 * @date 2023/4/19
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 后置处理属性值
     *
     * @param pvs      一个 bean 的所有字段及值
     * @param bean     bean
     * @param beanName bean名称
     * @return {@link PropertyValues}
     */
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName);

    /**
     * 在目标对象实例化之前调用，该方法的返回值类型是Object，我们可以返回任何类型的值。
     * 由于这个时候目标对象还未实例化，所以这个返回值可以用来代替原本该生成的目标对象的实例(比如代理对象)。
     * 如果该方法的返回值代替原本该生成的目标对象，后续只有postProcessAfterInitialization方法会调用，其它方法不再调用；否则按照正常的流程走。
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

}
