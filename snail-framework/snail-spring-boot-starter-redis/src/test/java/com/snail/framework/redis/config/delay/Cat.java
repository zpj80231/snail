package com.snail.framework.redis.config.delay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cat implements Serializable {
    private static final long serialVersionUID = -5126662920904485547L;
    private String name;
    private int age;
}