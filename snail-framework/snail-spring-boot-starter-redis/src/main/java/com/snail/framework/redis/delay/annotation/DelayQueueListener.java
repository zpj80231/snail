package com.snail.framework.redis.delay.annotation;

import com.snail.framework.redis.delay.domain.DelayMessage;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法上标识此注解，用于监听延迟队列中的消息。
 * <p>
 * 接收的方法参数可以有多个：<br/>
 * 但必须 <b>至少包含一个</b> {@link DelayMessage} 类型的参数，如以下示例中的 {@code DelayMessage<T> message}；<br/>
 * <b>或者至少包含一个</b> {@code T} 的具体的泛型类型，如以下示例中的 {@code Cat cat}；<br/>
 * 如果还有其他参数，那么多余参数无效，如以下示例中的 {@code String useless}；
 * <p>
 * 示例：
 * <p>
 * <pre>
 * {@code @DelayQueueListener({QUEUE_NAME_1, QUEUE_NAME_2})
 * public void consumer(DelayMessage<Object> message) {
 *     log.info("Received, message: {}", message);
 *     counter.decrementAndGet();
 * }
 * }
 * </pre>
 * <pre>
 * {@code @DelayQueueListener({QUEUE_NAME_1, QUEUE_NAME_2})
 * public void consumer(Cat cat) {
 *     log.info("Received, cat: {}", cat);
 *     counter.decrementAndGet();
 * }
 * }
 * </pre>
 *
 * @author zhangpengjun
 * @date 2024/5/7
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelayQueueListener {

    @AliasFor("queues")
    String[] value() default {};

    @AliasFor("value")
    String[] queues() default {};

}
