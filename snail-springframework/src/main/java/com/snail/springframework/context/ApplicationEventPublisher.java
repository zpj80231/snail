package com.snail.springframework.context;

import com.snail.springframework.context.event.ApplicationEvent;

/**
 * 定义事件发布者，所有的事件都需要从这个接口发布出去。
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public interface ApplicationEventPublisher {

    /**
     * 发布事件
     *
     * @param event 事件
     */
    void publishEvent(ApplicationEvent event);

}
