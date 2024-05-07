package com.springframework.beans.factory.event;

import com.springframework.context.event.ApplicationContextEvent;

/**
 * 自定义事件
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public class CustomEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = 2651556915889874385L;
    private String id;
    private String message;

    public CustomEvent(Object source, String id, String message) {
        super(source);
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CustomEvent{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", source=" + source +
                '}';
    }
}
