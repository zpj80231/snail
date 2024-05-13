package com.snail.framework.redis.delay.interceptor;

import com.snail.framework.redis.delay.domain.DelayMessage;

/**
 * 延迟消息生产者拦截处理器
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
public interface DelayMessageProducerInterceptor {

    /**
     * 拦截将要发送消息，进行处理或加工
     *
     * @param message 消息
     * @return boolean 是否继续发送消息，返回 false 将不发送消息
     */
    <T> boolean producerIntercept(DelayMessage<T> message);

}
