package com.springframework.convert;

import cn.hutool.core.date.DateUtil;
import com.springframework.core.convert.converter.Converter;

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
