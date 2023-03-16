package com.snail.springframework.core.io;

import cn.hutool.core.io.IoUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class DefaultResourceLoaderTest {

    private ResourceLoader resourceLoader;

    @Before
    public void init() {
        resourceLoader = new DefaultResourceLoader();
    }

    @Test
    public void test_classpath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:spring.xml");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println("===== classpath =====");
        System.out.println(content);
        System.out.println("===== classpath =====");
    }

    @Test
    public void test_file() throws IOException {
        Resource resource = resourceLoader.getResource("src/main/resources/spring.xml");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println("===== file =====");
        System.out.println(content);
        System.out.println("===== file =====");
    }

}
