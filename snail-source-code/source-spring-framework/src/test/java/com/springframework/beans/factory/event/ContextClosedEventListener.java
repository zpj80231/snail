package com.springframework.beans.factory.event;

import com.springframework.context.ApplicationListener;
import com.springframework.context.event.ContextClosedEvent;

/**
 * 监听容器关闭事件
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("收到事件 ContextClosedEvent：" + event);
    }

}
