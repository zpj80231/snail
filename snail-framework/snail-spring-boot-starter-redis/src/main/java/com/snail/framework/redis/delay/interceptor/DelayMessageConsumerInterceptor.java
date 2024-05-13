package com.snail.framework.redis.delay.interceptor;

import com.snail.framework.redis.delay.domain.DelayMessage;

/**
 * 延迟消息拦截处理器
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
public interface DelayMessageConsumerInterceptor {

    /**
     * 拦截收到的延迟消息（还未处理前），进行处理或加工
     *
     * @param message 消息
     * @return boolean 是否继续处理消息，返回 false 将不处理消息
     */
    <T> boolean consumerIntercept(DelayMessage<T> message);

}
