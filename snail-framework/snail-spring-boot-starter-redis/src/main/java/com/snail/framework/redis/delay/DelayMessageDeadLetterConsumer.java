package com.snail.framework.redis.delay;

import com.snail.framework.redis.delay.domain.DelayMessage;

/**
 * 延迟消息死信消费者
 * <br/>
 * 当延迟消息处理出现异常时，会触发这个函数，进行消息的最终处理
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
public interface DelayMessageDeadLetterConsumer {

   /**
    * 收到死信消息
    *
    * @param message 消息
    * @param e
    */
   <T> void receivedMessage(DelayMessage<T> message, Exception e);

}
