package com.snail.framework.redis.delay;

import com.alibaba.fastjson.JSON;
import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Slf4j
public class DefaultDelayMessageDeadLetterConsumer implements DelayMessageDeadLetterConsumer {

    @Override
    public <T> void receivedMessage(DelayMessage<T> message) {
        log.warn("dead letter message for queues:{}, message:{}", message.getQueues(), JSON.toJSONString(message));
    }

}
