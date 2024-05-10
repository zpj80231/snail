package com.springframework.beans;

/**
 * 在运行时动态地获取对象的实例
 *
 * @author zhangpengjun
 * @date 2023/9/19
 */
public interface ObjectFactory<T> {

    /**
     * 这个方法返回一个类型为 T 的对象实例，您可以通过调用 getObject 方法来获取。
     * ObjectFactory 通常用于将对象的创建和初始化推迟到需要的时候，以避免在应用程序启动时立即创建所有对象。
     * <p>
     * 在 Spring 中，ObjectFactory 的一个常见用法是在解决循环依赖问题时。
     * 当 Spring 需要创建一个 Bean，但该 Bean 正在创建中，因此还不能完全初始化时，它会将该 Bean 的 ObjectFactory 放入二级缓存（earlySingletonObjects）中，
     * 以便其他 Bean 可以提前获取 Bean 的引用，而不会导致循环依赖问题。
     * <p>
     * 另一个常见的用例是在配置中声明原型（prototype）作用域的 Bean，因为原型作用域的 Bean 不会在容器初始化时立即创建，而是在每次请求时动态创建。
     * 在这种情况下，ObjectFactory 可以用于获取原型作用域 Bean 的实例。
     *
     * @return {@link T}
     */
    T getObject();

}
