package com.snail.framework.redis.delay;

import cn.hutool.core.collection.CollUtil;
import com.snail.framework.redis.delay.annotation.DelayQueueListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列消费器注册器
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Slf4j
public class DefaultDelayQueueRegistry implements DelayQueueRegistry, BeanPostProcessor {

    private final Map<String, DelayMessageConsumerContainer> consumerContainerMap = new HashMap<>();

    @Override
    public Map<String, DelayMessageConsumerContainer> getConsumerContainers() {
        return consumerContainerMap;
    }

    @Override
    public DelayMessageConsumerContainer getConsumerContainer(String queueName) {
        return consumerContainerMap.get(queueName);
    }

    @Override
    public boolean containsConsumerContainer(String queueName) {
        return consumerContainerMap.containsKey(queueName);
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        Map<Method, DelayQueueListener> listenerMap = MethodIntrospector.selectMethods(targetClass,
                (MethodIntrospector.MetadataLookup<DelayQueueListener>) method -> AnnotationUtils.findAnnotation(method, DelayQueueListener.class));
        if (CollUtil.isNotEmpty(listenerMap)) {
            listenerMap.forEach((method, listener) -> {
                DelayMessageConsumerContainer consumerContainer = new DelayMessageConsumerContainer(bean, method, listener);
                consumerContainerMap.computeIfAbsent(listener.queues(), k -> consumerContainer);
            });
        }
        return bean;
    }

}
