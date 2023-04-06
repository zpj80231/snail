package com.snail.springframework.context.event;

import com.snail.springframework.context.ApplicationContext;

/**
 * ApplicationEvent 一个具体的事件，这个还比较顶层。
 * 所以我们再创建一个 ApplicationContextEvent 更具体的来表明这是一个容器事件。
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public class ApplicationContextEvent extends ApplicationEvent {

    private static final long serialVersionUID = -2652382590021151001L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationContextEvent(Object source) {
        super(source);
    }

    /**
     * 获取应用程序上下文，上下文肯定是容有参构造传递过来的
     *
     * @return {@link ApplicationContext}
     */
    public ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }

}
