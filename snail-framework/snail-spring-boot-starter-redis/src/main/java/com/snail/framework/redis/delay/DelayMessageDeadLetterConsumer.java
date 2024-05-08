package com.snail.framework.redis.delay;

import com.snail.framework.redis.delay.domain.DelayMessage;

/**
 * @author zhangpengjun
 * @date 2024/5/7
 */
public interface DelayMessageDeadLetterConsumer {

   <T> void receivedMessage(DelayMessage<T> message);

}
