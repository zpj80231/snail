package com.springframework.context;

import com.springframework.beans.factory.HierarchicalBeanFactory;
import com.springframework.beans.factory.ListableBeanFactory;
import com.springframework.core.io.ResourceLoader;

/**
 * 容器顶层接口
 * 与 BeanFactory 不同，ApplicationContext 在容器启动时会一次性创建并初始化所有的 Bean 实例，而不是在使用时再进行创建。
 * 这意味着，ApplicationContext 在应用程序启动时会更快地启动和准备好，同时也减少了在运行时创建 Bean 实例的开销。
 * 此外，ApplicationContext 还提供了更多的特性，例如事件发布、国际化、AOP 等。
 *
 * @author zhangpengjun
 * @date 2023/3/21
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {

}
