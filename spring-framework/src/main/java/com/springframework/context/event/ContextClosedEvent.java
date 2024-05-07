package com.springframework.context.event;

/**
 * 容器关闭事件
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public class ContextClosedEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = 9130139349987597886L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextClosedEvent(Object source) {
        super(source);
    }

}
