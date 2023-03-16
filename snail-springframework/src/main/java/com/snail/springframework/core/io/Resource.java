package com.snail.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 资源加载器
 *
 * @author zhangpengjun
 * @date 2023/3/16
 */
public interface Resource {

    /**
     * 获取资源流
     *
     * @return {@link InputStream}
     * @throws IOException ioexception
     */
    InputStream getInputStream() throws IOException;

}
