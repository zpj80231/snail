package com.springframework.core.convert.support;

import com.springframework.core.convert.converter.ConverterRegistry;

/**
 * @author zhangpengjun
 * @date 2023/9/20
 */
public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(ConverterRegistry converterRegistry) {
        // 添加各类型转换工厂
        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
    }

}
