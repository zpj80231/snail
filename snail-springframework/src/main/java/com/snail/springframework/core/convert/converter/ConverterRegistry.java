package com.snail.springframework.core.convert.converter;

/**
 * 类型转换器注册器接口
 *
 * @author zhangpengjun
 * @date 2023/9/20
 */
public interface ConverterRegistry {

    /**
     * 添加转换器
     *
     * @param converter 转换器
     */
    void addConverter(Converter<?, ?> converter);

    /**
     * 添加转换器
     *
     * @param converter 转换器
     */
    void addConverter(GenericConverter converter);

    /**
     * 添加转换器工厂
     *
     * @param converterFactory 转换器工厂
     */
    void addConverterFactory(ConverterFactory<?, ?> converterFactory);

}
