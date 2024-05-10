package com.springframework.context.support;

import com.springframework.beans.factory.FactoryBean;
import com.springframework.beans.factory.InitializingBean;
import com.springframework.core.convert.ConversionService;
import com.springframework.core.convert.converter.Converter;
import com.springframework.core.convert.converter.ConverterFactory;
import com.springframework.core.convert.converter.ConverterRegistry;
import com.springframework.core.convert.converter.GenericConverter;
import com.springframework.core.convert.support.DefaultConversionService;
import com.springframework.core.convert.support.GenericConversionService;
import com.sun.istack.internal.Nullable;

import java.util.Set;

/**
 * 创建 ConversionService 工厂
 *
 * @author zhangpengjun
 * @date 2023/9/20
 */
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    @Nullable
    private Set<?> converters;

    @Nullable
    private GenericConversionService conversionService;

    @Override
    public ConversionService getObject() {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiedSet() {
        this.conversionService = new DefaultConversionService();
        registerConverters(converters, conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegistry registry) {
        if (converters == null) {
            return;
        }
        for (Object converter : converters) {
            if (converter instanceof GenericConverter) {
                registry.addConverter((GenericConverter) converter);
            } else if (converter instanceof Converter<?, ?>) {
                registry.addConverter((Converter<?, ?>) converter);
            } else if (converter instanceof ConverterFactory<?, ?>) {
                registry.addConverterFactory((ConverterFactory<?, ?>) converter);
            } else {
                throw new IllegalArgumentException("Each converter object must implement one of the " +
                        "Converter, ConverterFactory, or GenericConverter interfaces");
            }
        }
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }

}
