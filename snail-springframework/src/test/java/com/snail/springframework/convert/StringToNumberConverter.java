package com.snail.springframework.convert;

import cn.hutool.core.convert.Convert;
import com.snail.springframework.core.convert.converter.Converter;

/**
 * @author zhangpengjun
 * @date 2023/9/21
 */
public class StringToNumberConverter implements Converter<String, Number> {

    @Override
    public Number convert(String source) {
        return Convert.convert(Number.class, source);
    }

}
