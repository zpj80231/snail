package com.snail.springframework.beans.factory;

import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.PropertyValue;
import com.snail.springframework.beans.PropertyValues;
import com.snail.springframework.beans.factory.config.BeanDefinition;
import com.snail.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.snail.springframework.core.io.DefaultResourceLoader;
import com.snail.springframework.util.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * bean对象 占位符处理 <br/>
 * 从给定的配置文件加载占位符的 key value。
 * 在 bean 实例化之前，替换 xml 配置的属性表达式。
 * 在 bean 实例化之后，属性赋值之前，替换 @Value 配置的属性表达式。
 *
 * @author zhangpengjun
 * @date 2023/9/13
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    /**
     * 默认占位符前缀
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * 默认占位符后缀
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // 加载属性文件
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Properties properties = new Properties();
        try {
            properties.load(resourceLoader.getResource(location).getInputStream());
        } catch (IOException e) {
            throw new BeansException("Could not load properties", e);
        }

        // 遍历所有bean的属性值，符合规则的替换
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                Object value = propertyValue.getValue();
                if (!(value instanceof String)) {
                    continue;
                }
                String strValue = (String) value;
                strValue = resolvePlaceholder(strValue, properties);
                propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), strValue));
            }
        }

        // 向容器中添加字符串解析器，供解析@Value使用
        StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
        beanFactory.addEmbeddedValueResolver(valueResolver);
    }

    private static String resolvePlaceholder(String strValue, Properties properties) {
        StringBuilder builder = new StringBuilder(strValue);
        int startIdx = strValue.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int stopIdx = strValue.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
            String key = strValue.substring(startIdx + 2, stopIdx);
            String val = properties.getProperty(key);
            builder.replace(startIdx, stopIdx + 1, val);
        }
        return builder.toString();
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private static class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String value) {
            return resolvePlaceholder(value, properties);
        }

    }
}
