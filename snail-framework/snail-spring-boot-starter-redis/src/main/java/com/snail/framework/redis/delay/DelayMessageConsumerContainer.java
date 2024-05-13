package com.snail.framework.redis.delay;

import cn.hutool.core.util.StrUtil;
import com.snail.framework.redis.delay.annotation.DelayQueueListener;
import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 延迟消息消费器容器，封装了具体的消费器、监听器。
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Slf4j
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
        method.invoke(bean, message);
    }

}
