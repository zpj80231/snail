package com.snail.framework.redis.delay.interceptor;

import com.alibaba.fastjson.JSON;
import com.snail.framework.redis.delay.DelayQueue;
import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 默认延迟消息生成器拦截器
 * <br/>
 * 如果延迟消息的队列并没有消费器，那么消息将不会发送
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Slf4j
public class DefaultDelayMessageProducerInterceptor implements DelayMessageProducerInterceptor {

    private final DelayQueue delayQueue;

    public DefaultDelayMessageProducerInterceptor(DelayQueue delayQueue) {
        this.delayQueue = delayQueue;
    }

    @Override
    public <T> boolean producerIntercept(DelayMessage<T> message) {
        List<String> queues = message.getQueues();
        for (String queue : queues) {
            if (!delayQueue.containsConsumerContainer(queue)) {
                log.error("No consumer container found for queue: {}, message: {}", queue, JSON.toJSONString(message));
                return false;
            }
        }
        return true;
    }

}
