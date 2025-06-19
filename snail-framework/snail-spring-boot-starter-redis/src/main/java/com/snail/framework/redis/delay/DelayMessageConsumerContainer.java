package com.snail.framework.redis.delay;

import cn.hutool.core.util.StrUtil;
import com.snail.framework.redis.delay.annotation.DelayQueueListener;
import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 延迟消息消费器容器，封装了具体的消费器、监听器。
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Slf4j
@Getter
public class DelayMessageConsumerContainer {

    private final Object bean;

    private final Method method;

    private final DelayQueueListener delayQueueListener;

    public DelayMessageConsumerContainer(Object bean, Method method, DelayQueueListener delayQueueListener) {
        this.bean = bean;
        this.method = method;
        this.delayQueueListener = delayQueueListener;
        String[] queues = delayQueueListener.queues();
        for (String queue : queues) {
            if (StrUtil.isBlank(queue)) {
                throw new IllegalArgumentException("DelayQueueListener queue cannot be empty");
            }
        }
    }

    public <T> void invoke(DelayMessage<T> message) throws InvocationTargetException, IllegalAccessException {
        if (message == null) {
            return;
        }
        T messageBody = message.getBody();
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        boolean matched = false;
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType().isAssignableFrom(DelayMessage.class)) {
                args[i] = message;
                matched = true;
            } else if (messageBody != null && parameters[i].getType().isAssignableFrom(messageBody.getClass())) {
                args[i] = messageBody;
                matched = true;
            }
        }
        if (!matched) {
            throw new IllegalArgumentException("Method parameter type mismatch: Expected type DelayMessage<T> or " +
                    (messageBody == null ? "T" : messageBody.getClass().getSimpleName()));
        }
        method.invoke(bean, args);
    }

    @Override
    public String toString() {
        String className = bean.getClass().getName();
        String methodName = method.getName();
        String paramTypes = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
        int beanHash = System.identityHashCode(bean);

        return String.format("{class=%s, method=%s(%s), beanHash=%d}",
                className, methodName, paramTypes, beanHash);
    }

}
