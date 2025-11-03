package com.snail.source.mybatis.io;

import java.io.InputStream;

/**
 * 资源加载类
 *
 * @author zhangpj
 * @date 2025/10/27
 */
public class Resources {

    /**
     * 加载指定配置文件，获取资源作为流返回
     *
     * @param path 路径
     * @return {@link InputStream }
     * @throws Exception 异常
     */
    public static InputStream getResourceAsStream(String path) throws Exception {
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }

}
