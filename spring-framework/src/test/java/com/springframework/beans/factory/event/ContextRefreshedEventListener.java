package com.springframework.beans.factory.event;

import com.springframework.context.ApplicationListener;
import com.springframework.context.event.ContextRefreshedEvent;

/**
 * 监听容器刷新完成事件
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("收到事件 ContextRefreshedEvent：" + event);
    }

}
