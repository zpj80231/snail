package com.snail.springframework.context;

/**
 * 扩展了 ApplicationContext 的功能
 * 在这里定义了大名鼎鼎的刷新（refresh）功能
 * 另外，ConfigurableApplicationContext 还提供了一些工具方法，例如获取系统环境变量、获取资源、获取 Bean 类型、启动和关闭容器等。
 *
 * @author zhangpengjun
 * @date 2023/3/21
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新容器
     */
    void refresh();

}
