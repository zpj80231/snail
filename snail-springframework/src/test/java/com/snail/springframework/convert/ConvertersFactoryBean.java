package com.snail.springframework.convert;

import com.snail.springframework.beans.factory.FactoryBean;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangpengjun
 * @date 2023/9/21
 */
public class ConvertersFactoryBean implements FactoryBean<Set<?>> {

    @Override
    public Set<?> getObject() {
        HashSet<Object> converts = new HashSet<>();
        StringToDateConverter stringToDateConverter = new StringToDateConverter();
        converts.add(stringToDateConverter);
        return converts;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
