package com.snail.springframework.context;

import com.snail.springframework.context.event.ApplicationEvent;

import java.util.EventListener;

/**
 * 事件监听器
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 事件触发时会回调这个方法
     *
     * @param event 事件
     */
    void onApplicationEvent(E event);

}
