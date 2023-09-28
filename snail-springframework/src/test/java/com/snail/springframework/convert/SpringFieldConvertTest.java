package com.snail.springframework.convert;

import com.snail.springframework.context.support.ClassPathXmlApplicationContext;
import com.snail.springframework.core.convert.converter.Converter;
import com.snail.springframework.core.convert.support.StringToNumberConverterFactory;
import org.junit.Test;

/**
 * @author zhangpengjun
 * @date 2023/9/13
 */
public class SpringFieldConvertTest {

    /**
     * 测试三级缓存循环引用
     */
    @Test
    public void test_field_convert() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-field-convert.xml");
        FieldConvertPO fieldConvertPO = applicationContext.getBean(FieldConvertPO.class);
        System.out.println("测试结果：" + fieldConvertPO);
        // 测试结果：FieldConvertPO{name='FieldConvertPOName', date=Tue Oct 10 10:10:10 CST 2000}
    }

    @Test
    public void test_StringToNumberConverter() {
        StringToNumberConverter stringToNumberConverter = new StringToNumberConverter();
        Number n1 = stringToNumberConverter.convert("123");
        System.out.println("测试结果：" + n1);
        Number n2 = stringToNumberConverter.convert("123.12");
        System.out.println("测试结果：" + n2);
        // 测试结果：123
        // 测试结果：123.12
    }

    @Test
    public void test_StringToNumberConverterFactory() {
        StringToNumberConverterFactory converterFactory = new StringToNumberConverterFactory();
        Converter<String, Integer> integerConverter = converterFactory.getConverter(Integer.class);
        Number n1 = integerConverter.convert("123.45");
        System.out.println("测试结果：" + n1);
        Converter<String, Long> longConverter = converterFactory.getConverter(Long.class);
        Number n2 = longConverter.convert("123.12");
        System.out.println("测试结果：" + n2);
        Converter<String, Double> doubleConverter = converterFactory.getConverter(Double.class);
        Number n3 = doubleConverter.convert("123.78");
        System.out.println("测试结果：" + n3);
        // 测试结果：123
        // 测试结果：123
        // 测试结果：123.78
    }

}
