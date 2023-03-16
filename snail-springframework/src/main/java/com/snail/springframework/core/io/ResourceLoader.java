package com.snail.springframework.core.io;

/**
 * 资源加载器包装器
 * 相当于 Resource 的工厂
 *
 * @author zhangpengjun
 * @date 2023/3/16
 */
public interface ResourceLoader {

    /**
     * 类路径url前缀
     */
    String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * 从指定位置获得资源
     *
     * @param location 可以是url，或文件路径，或类路径（以 classpath: 开头）
     * @return {@link Resource}
     */
    Resource getResource(String location);

}
