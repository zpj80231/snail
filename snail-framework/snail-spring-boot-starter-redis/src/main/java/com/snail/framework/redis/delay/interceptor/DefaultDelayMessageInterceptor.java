package com.snail.framework.redis.delay.interceptor;

import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认延迟消息生成器处理器
 * <br/>
 * 如果延迟消息的队列并没有消费器，那么消息将不会发送
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Slf4j
public class DefaultDelayMessageInterceptor implements DelayMessageInterceptor {

    private final DelayMessageProducerInterceptor  producerInterceptor;
    private final DelayMessageConsumerInterceptor  consumerInterceptor;

    public DefaultDelayMessageInterceptor(DelayMessageProducerInterceptor producerInterceptor,
                                          DelayMessageConsumerInterceptor consumerInterceptor) {
        this.producerInterceptor = producerInterceptor;
        this.consumerInterceptor = consumerInterceptor;
    }

    @Override
    public <T> boolean producerIntercept(DelayMessage<T> message) {
        return producerInterceptor.producerIntercept(message);
    }

    @Override
    public <T> boolean consumerIntercept(DelayMessage<T> message) {
        return consumerInterceptor.consumerIntercept(message);
    }

}
