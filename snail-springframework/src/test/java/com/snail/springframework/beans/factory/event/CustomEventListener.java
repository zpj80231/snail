package com.snail.springframework.beans.factory.event;

import com.snail.springframework.context.ApplicationListener;

/**
 * 自定义事件监听
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {

    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("收到事件 CustomEvent：" + event);
    }

}
