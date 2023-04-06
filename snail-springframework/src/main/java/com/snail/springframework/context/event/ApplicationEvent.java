package com.snail.springframework.context.event;

import java.util.EventObject;

/**
 * 一个具体的事件
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public abstract class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = 5949591757825084930L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationEvent(Object source) {
        super(source);
    }

}
