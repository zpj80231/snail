package com.springframework.core.convert.support;

import cn.hutool.core.convert.Convert;
import com.springframework.core.convert.converter.Converter;
import com.springframework.core.convert.converter.ConverterFactory;
import com.sun.istack.internal.Nullable;

/**
 * @author zhangpengjun
 * @date 2023/9/20
 */
public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {


    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber<>(targetType);
    }

    private static final class StringToNumber<T extends Number> implements Converter<String, T> {

        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        @Nullable
        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }
            return Convert.convert(targetType, source);
        }
    }

}
