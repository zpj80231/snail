package com.springframework.core.convert.converter;

/**
 * 类型转换处理器接口
 *
 * @author zhangpengjun
 * @date 2023/9/20
 */
public interface Converter<S, T> {

    /**
     * 将类型为 {@code S} 的源对象转换为目标类型 {@code T}
     *
     * @param source 来源
     * @return {@link T}
     */
    T convert(S source);

}
