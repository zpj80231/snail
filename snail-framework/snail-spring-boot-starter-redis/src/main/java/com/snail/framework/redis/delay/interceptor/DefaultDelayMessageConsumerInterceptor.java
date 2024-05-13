package com.snail.framework.redis.delay.interceptor;

import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认延迟消息消费器拦截器
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Slf4j
public class DefaultDelayMessageConsumerInterceptor implements DelayMessageConsumerInterceptor {

    @Override
    public <T> boolean consumerIntercept(DelayMessage<T> message) {
        return true;
    }
}
