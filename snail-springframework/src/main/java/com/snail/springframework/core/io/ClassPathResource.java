package com.snail.springframework.core.io;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 类路径资源，可以从类路径下读取资源
 *
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class ClassPathResource implements Resource {

    private final String path;

    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "Path must not be null");
        this.path = path;
        this.classLoader = classLoader == null ? ClassUtil.getClassLoader() : classLoader;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(this.path + " cannot be opened because it does not exist");
        }
        return is;
    }

}
