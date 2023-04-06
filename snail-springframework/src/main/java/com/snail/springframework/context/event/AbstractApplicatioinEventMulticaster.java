package com.snail.springframework.context.event;

import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.factory.BeanFactory;
import com.snail.springframework.beans.factory.BeanFactoryAware;
import com.snail.springframework.context.ApplicationEventMulticaster;
import com.snail.springframework.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 默认实现多播器中事件监听器的注册、添加以及获取指定事件监听器的方法，把发布事件留给不同子类实现，以方便不同的扩展。
 *
 * @author zhangpengjun
 * @date 2023/3/31
 */
public abstract class AbstractApplicatioinEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    private final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    private BeanFactory beanFactory;

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 获取订阅了某个事件的所有监听器：遍历所有的监听器，看监听器泛型是否和指定事件所匹配
     *
     * @param event 事件
     * @return {@link Collection}<{@link ApplicationListener}>
     */
    protected Collection<ApplicationListener<ApplicationEvent>> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener<ApplicationEvent>> allListeners = new LinkedList<>();
        for (ApplicationListener<ApplicationEvent> listener : applicationListeners) {
            if (supportsEvent(listener, event)) {
                allListeners.add(listener);
            }
        }
        return allListeners;
    }

    private boolean supportsEvent(ApplicationListener<ApplicationEvent> listener, ApplicationEvent event) {
        Class<? extends ApplicationListener> listenerClass = listener.getClass();
        // 主要是用来获取当前类的接口，但当前类创建时可能是cglib或jdk代理实现的
        // jdk动态代理的代理类已经实现了接口，直接使用该 Class 对象即可。
        Class<?> targetClass = listenerClass;
        // 判断是否是 cglib 代理类，如果是，就获取其父类的 Class 对象，否则直接使用该 Class 对象。
        if (listenerClass.getName().contains("$$")) {
            targetClass = listenerClass.getSuperclass();
        }

        // 获取事件监听器实现的第一个泛型接口（即 ApplicationListener）
        Type genericInterfaces = targetClass.getGenericInterfaces()[0];

        // 获取该接口的实际泛型参数类型，因为只有一个取第一个即可。
        Type actualTypeArgument = ((ParameterizedType) genericInterfaces).getActualTypeArguments()[0];

        // 获取泛型的全类名并得到Class，用于事件类型对比
        String eventClassName = actualTypeArgument.getTypeName();
        Class<?> eventClass;
        try {
            eventClass = Class.forName(eventClassName);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong evnet class name: " + eventClassName);
        }

        // 判断泛型和事件是否匹配
        // 父类.class.isAssignableFrom(子类.class)
        // 子类实例 instanceof 父类类型
        return eventClass.isAssignableFrom(event.getClass());
    }

}
