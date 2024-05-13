package com.snail.framework.redis.delay;

import com.alibaba.fastjson.JSON;
import com.snail.framework.redis.delay.domain.DelayMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 延迟消息死信消息处理器，默认的
 * <br/>
 * 当延迟消息处理出现异常时，会触发这个函数，进行消息的最终处理
 *
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
