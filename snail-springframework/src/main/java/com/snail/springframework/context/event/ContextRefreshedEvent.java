package com.snail.springframework.context.event;

/**
 * 容器刷新完成事件
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = 5264064757132179583L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextRefreshedEvent(Object source) {
        super(source);
    }

}
