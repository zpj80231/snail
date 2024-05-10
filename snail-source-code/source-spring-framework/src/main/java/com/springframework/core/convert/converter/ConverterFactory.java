package com.springframework.core.convert.converter;

/**
 * 类型转换工厂
 *
 * @author zhangpengjun
 * @date 2023/9/20
 */
public interface ConverterFactory<S, R> {

    /**
     * 获取指定转换器，转换器可以把 S 转换为目标类型 T，其中 T 也是 R 的一个实例。
     *
     * @param targetType 目标类型
     * @return {@link Converter}<{@link S}, {@link T}>
     */
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);

}
