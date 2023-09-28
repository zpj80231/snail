package com.snail.springframework.core.convert.converter;

import java.util.Objects;
import java.util.Set;

/**
 * 通用转换器接口
 *
 * @author zhangpengjun
 * @date 2023/9/20
 */
public interface GenericConverter {

    /**
     * 获取 转换类型对
     *
     * @return {@link Set}<{@link ConvertiblePair}>
     */
    Set<ConvertiblePair> getConvertibleTypes();

    /**
     * 转换
     *
     * @param source     来源
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return {@link Object}
     */
    Object convert(Object source, Class<?> sourceType, Class<?> targetType);


    final class ConvertiblePair {
        private final Class<?> sourceType;
        private final Class<?> targetType;

        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public Class<?> getSourceType() {
            return sourceType;
        }

        public Class<?> getTargetType() {
            return targetType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConvertiblePair that = (ConvertiblePair) o;
            return Objects.equals(sourceType, that.sourceType) && Objects.equals(targetType, that.targetType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceType, targetType);
        }
    }

}
