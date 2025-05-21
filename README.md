# snail

🐌 在最高点乘着叶片往前飞～

## 源码系列

从零开始逐步实现几大常用框架。通过手写这些内容，不仅能够深入理解它们的工作方式和设计理念，还能显著提升我们的编码能力。

## 手写 Spring

- [手写 Spring 01 - 核心容器实现](https://zpj80231.github.io/znote/views/backend/spring-source-01.html)
- [手写 Spring 02 - 添加外部资源加载器以及和现有容器结合](https://zpj80231.github.io/znote/views/backend/spring-source-02.html)
- [手写 Spring 03 - 扩展 Bean 的生命周期](https://zpj80231.github.io/znote/views/backend/spring-source-03.html)
- [手写 Spring 04 - 加入上下文，预加载容器操作](https://zpj80231.github.io/znote/views/backend/spring-source-04.html)
- [手写 Spring 05 - 完善Bean生命周期，加入Bean的初始化和销毁](https://zpj80231.github.io/znote/views/backend/spring-source-05.html)
- [手写 Spring 06 - Aware扩展，感知容器各个组件](https://zpj80231.github.io/znote/views/backend/spring-source-06.html)
- [手写 Spring 07 - 完善 Bean 作用域和实现 FactoryBean](https://zpj80231.github.io/znote/views/backend/spring-source-07.html)
- [手写 Spring 08 - 添加事件多播器，支持容器事件的注册、订阅、发布](https://zpj80231.github.io/znote/views/backend/spring-source-08.html)
- [手写 Spring 09 - 核心增强，AOP实现](https://zpj80231.github.io/znote/views/backend/spring-source-09.html)
- [手写 Spring 10 - AOP 和 Spring 容器的整合](https://zpj80231.github.io/znote/views/backend/spring-source-10.html)
- [手写 Spring 11 - Bean注解方式扫描，Bean属性xml解析替换](https://zpj80231.github.io/znote/views/backend/spring-source-11.html)
- [手写 Spring 12 - 实现 @Autowired,@Qualifier,@Value](https://zpj80231.github.io/znote/views/backend/spring-source-12.html)
- [手写 Spring 13 - 改造代理对象的创建过程及代理对象的属性填充](https://zpj80231.github.io/znote/views/backend/spring-source-13.html)
- [手写 Spring 14 - 循环引用及三级缓存](https://zpj80231.github.io/znote/views/backend/spring-source-14.html)
- [手写 Spring 15 - 数据类型转换](https://zpj80231.github.io/znote/views/backend/spring-source-15.html)

## 手写 JDBC 连接池

- [手写 JDBC 连接池](https://zpj80231.github.io/znote/views/backend/jdbc-pool-source-01.html)

## 手写 Netty RPC

- 基础定义：
  - 自定义传输协议
  - 自定义编解码器
  - 传输消息体
  - 序列化算法（可扩展）
- 服务端（服务暴露）：
  - 自动扫描：基于注解
  - 自动注册：基于注解和注册中心（当然可扩展nacos或zk或其它注册中心）
  - 注册中心：默认本地注册（纯手写，模拟zk的文件路径方式）
- 客户端（服务发现）：
  - 自动发现：基于注册中心
  - 负载均衡：随机、轮询（可扩展）
  - 连接复用：存在多个服务端时，对每个服务端只需复用一个连接
  - 服务代理：调用方无感知，代理服务调用
  - 远程调用：通过自定义rpc进行远程调用
  - 多实例启动：可同时启动多个服务端，模拟多实例场景，以供客户端负载均衡
  - 优雅关闭：无可用rpc连接时（如多个服务端全部宕机），自动关闭服务



