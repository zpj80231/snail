package com.snail.springframework.util;

/**
 * 字符串解析器，实现这个接口的类都注入到容器中，方便解析 @Value 使用。
 *
 * @author zhangpengjun
 * @date 2023/9/15
 */
public interface StringValueResolver {

    /**
     * 解析字符串值
     *
     * @param value 要解析的字段的属性值
     * @return {@link String} 替换后的值
     */
    String resolveStringValue(String value);

}
