package com.springframework.beans;

/**
 * 主要用于 Bean 的属性填充
 * 将一个 Bean 本身的 字段名、字段值映射为一个 PropertyValue
 *
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class PropertyValue {

    /**
     * Bean 的一个字段属性名称
     */
    private String name;
    /**
     * Bean 的一个字段属性值
     */
    private Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
