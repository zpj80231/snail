package com.snail.springframework.beans;

/**
 * @author zhangpengjun
 * @date 2023/3/15
 */
public class BeansException extends RuntimeException {

    private static final long serialVersionUID = -40934857030114658L;

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }

}
