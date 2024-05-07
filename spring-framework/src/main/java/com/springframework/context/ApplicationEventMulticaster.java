package com.springframework.context;

import com.springframework.context.event.ApplicationEvent;

/**
 * applicatioin 事件多播器
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public interface ApplicationEventMulticaster {

    /**
     * 添加一个监听器
     *
     * @param listener 监听器
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 删除一个监听器
     *
     * @param listener 监听器
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 发布一个事件
     *
     * @param event 事件
     */
    void multicastEvent(ApplicationEvent event);

}
