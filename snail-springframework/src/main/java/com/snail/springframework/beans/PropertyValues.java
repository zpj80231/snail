package com.snail.springframework.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要用于 Bean 的属性填充
 * 当一个 Bean 有多个字段时，将 PropertyValue 保存为一个集合
 *
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class PropertyValues {

    /**
     * 属性值列表
     */
    private List<PropertyValue> propertyValueList = new ArrayList<>();

    /**
     * 添加属性值
     *
     * @param propertyValue 属性值
     */
    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValueList.add(propertyValue);
    }

    /**
     * 根据属性名（字段名）获取这个属性
     *
     * @param propertyName 属性名
     * @return {@link PropertyValue}
     */
    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : propertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    /**
     * 获取一个 Bean 所有的属性
     *
     * @return {@link PropertyValue[]}
     */
    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[propertyValueList.size()]);
    }

}
