package com.snail.springframework.convert;

import cn.hutool.core.date.DateUtil;
import com.snail.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * @author zhangpengjun
 * @date 2023/9/21
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        return DateUtil.parse(source);
    }

}
