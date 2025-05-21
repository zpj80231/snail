package com.snail.source.code.jdbc.pool.properties;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

/**
 * @author zhangpengjun
 * @date 2024/8/21
 */
@Data
public class DataSourceProperties {

    private String driver;
    private String url;
    private String username;
    private String password;
    private Integer initSize;
    private Integer maxSize;
    private Boolean health;
    private Long delay;
    private Long period;
    private Integer timeout;

    public DataSourceProperties() {
        Properties properties = new Properties();
        try {
            properties.load(DataSourceProperties.class.getClassLoader().getResourceAsStream("db.properties"));
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                String fieldName = key.toString().replace("jdbc.", "");

                Field field = this.getClass().getDeclaredField(fieldName);
                Class<?> fieldType = field.getType();
                Object convertedValue = convertValue(value, fieldType);

                String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = this.getClass().getDeclaredMethod(methodName, field.getType());
                method.invoke(this, convertedValue);            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object convertValue(Object value, Class<?> fieldType) {
        if (value == null && !fieldType.isPrimitive()) {
            return null;
        } else if (value == null) {
            return null;
        } else if (fieldType == Integer.TYPE || fieldType == Integer.class) {
            return Integer.parseInt((String) value);
        } else if (fieldType == Long.TYPE || fieldType == Long.class) {
            return Long.parseLong((String) value);
        } else if (fieldType == Boolean.TYPE || fieldType == Boolean.class) {
            return Boolean.parseBoolean((String) value);
        } else if (fieldType == Float.TYPE || fieldType == Float.class) {
            return Float.parseFloat((String) value);
        } else if (fieldType == Double.TYPE || fieldType == Double.class) {
            return Double.parseDouble((String) value);
        } else if (fieldType == String.class) {
            return value.toString();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + fieldType.getName());
        }
    }

}
