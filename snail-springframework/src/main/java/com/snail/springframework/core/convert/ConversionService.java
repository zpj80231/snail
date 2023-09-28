package com.snail.springframework.core.convert;

import com.sun.istack.internal.Nullable;

/**
 * 类型转换服务接口
 *
 * @author zhangpengjun
 * @date 2023/9/20
 */
public interface ConversionService {

    /**
     * 是否可以转换
     *
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return boolean
     */
    boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType);

    /**
     * 转换
     *
     * @param source     源
     * @param targetType 目标类型
     * @return {@link T}
     */
    <T> T convert(Object source, Class<T> targetType);

}
