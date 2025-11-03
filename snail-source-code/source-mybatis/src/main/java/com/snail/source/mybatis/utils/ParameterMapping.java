package com.snail.source.mybatis.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParameterMapping {

    /**
     * 保存 #{} 中对应的字段名称
     */
    private String content;

}