package com.snail.springframework.context;

import com.snail.springframework.beans.factory.Aware;

/**
 * 感知 应用程序上下文
 *
 * @author zhangpengjun
 * @date 2023/3/28
 */
public interface ApplicationContextAware extends Aware {

    /**
     * 设置应用程序上下文
     *
     * @param applicationContext 应用程序上下文
     */
    void setApplicationContext(ApplicationContext applicationContext);

}
