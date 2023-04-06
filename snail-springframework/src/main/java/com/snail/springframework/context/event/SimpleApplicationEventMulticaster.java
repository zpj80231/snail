package com.snail.springframework.context.event;

import com.snail.springframework.context.ApplicationListener;

/**
 * 使用同步方式调用每个监听器的 onApplicationEvent 方法。
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicatioinEventMulticaster {

    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener<ApplicationEvent> listener : getApplicationListeners(event)) {
            listener.onApplicationEvent(event);
        }
    }

}
