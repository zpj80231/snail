# snail

在最高点乘着叶片往前飞～ 🐌

## 简介

本项目致力于通过手写实现主流框架的核心功能，带你深入理解其底层原理与设计思想。在动手实践的过程中，不仅能够加深对源码的理解，还能有效提升架构能力与编码水平。

## 手写 Spring

手写 Spring 旨在通过一步步实现 Spring 框架的核心功能，深入理解其底层原理与设计思想。每一章节都会带你从零构建，帮助你掌握 Spring 的精髓（核心容器、生命周期管理、AOP、注解驱动等关键模块），提升架构能力与编码水平。通过亲手实现这些内容，不仅能加深对源码的理解，还能在实际开发中灵活运用相关设计思想与模式。

- [手写 Spring 01 - 核心容器实现](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-01.html)
- [手写 Spring 02 - 添加外部资源加载器以及和现有容器结合](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-02.html)
- [手写 Spring 03 - 扩展 Bean 的生命周期](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-03.html)
- [手写 Spring 04 - 加入上下文，预加载容器操作](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-04.html)
- [手写 Spring 05 - 完善Bean生命周期，加入Bean的初始化和销毁](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-05.html)
- [手写 Spring 06 - Aware扩展，感知容器各个组件](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-06.html)
- [手写 Spring 07 - 完善 Bean 作用域和实现 FactoryBean](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-07.html)
- [手写 Spring 08 - 添加事件多播器，支持容器事件的注册、订阅、发布](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-08.html)
- [手写 Spring 09 - 核心增强，AOP实现](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-09.html)
- [手写 Spring 10 - AOP 和 Spring 容器的整合](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-10.html)
- [手写 Spring 11 - Bean注解方式扫描，Bean属性xml解析替换](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-11.html)
- [手写 Spring 12 - 实现 @Autowired,@Qualifier,@Value](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-12.html)
- [手写 Spring 13 - 改造代理对象的创建过程及代理对象的属性填充](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-13.html)
- [手写 Spring 14 - 循环引用及三级缓存](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-14.html)
- [手写 Spring 15 - 数据类型转换](https://zpj80231.github.io/znote/views/source/code/spring/spring-source-15.html)

## 手写 Spring Boot Starter

本 Starter 提供即插即用的分布式能力，采用注解驱动的方式，集成简单、无额外依赖，助力 Spring Boot 项目轻松实现分布式锁、防重、缓存、延时队列等功能。

- [手写 Spring Boot Starter 01：基于 Redis 的分布式服务增强（分布式锁、防重、二级缓存、延时队列）实践指南](https://zpj80231.github.io/znote/views/source/code/starter/spring-boot-starter-redis-01.html)
- [手写 Spring Boot Starter 02：基于 Redis 的分布式服务增强（分布式锁、防重、二级缓存、延时队列）深入解析](https://zpj80231.github.io/znote/views/source/code/starter/spring-boot-starter-redis-02.html)

1.  **分布式锁**：通过 `@Lock` 注解实现声明式加锁，支持超时或阻塞等待，轻松解决并发控制问题。
2.  **防重攻击**：利用 `@DuplicateSubmit` 注解，基于用户指纹或请求参数生成唯一标识，有效防止重复提交，保障业务数据一致性。
3.  **二级缓存**：借助 `@DoubleCache` 注解，无缝整合 Caffeine（本地缓存）与 Redis（分布式缓存），提供高性能的读写体验，全面优化高并发场景下的数据访问效率。
4.  **延时队列**：基于 Redis ZSet 实现消息的延时投递与消费。通过 `DelayQueue` 发送消息，并使用 `@DelayQueueListener` 简洁地处理延时消息，满足各类延时消息场景。

## 手写 JDBC 连接池

手写 JDBC 简单连接池旨在理解数据库连接池的底层实现原理。通过一步步实现连接池的核心功能（如连接复用、池化管理、动态扩容、超时回收等），你将掌握数据库连接池的设计思想与关键技术点。项目简单，代码结构清晰，易于扩展，适合入门学习。通过亲手实现这些内容，不仅能加深对 JDBC 及数据库连接管理的理解，还能提升系统性能优化与架构设计能力。

- [手写 JDBC 连接池](https://zpj80231.github.io/znote/views/source/code/jdbc/jdbc-pool-source-01.html)

## 手写 Netty RPC

手写 Netty RPC 框架，旨在理解分布式服务调用的底层原理与实现细节。本项目不仅实现了自定义协议、编解码、序列化、注册中心、负载均衡等核心功能，还支持与 Spring 深度集成，开箱即用，便于二次开发和扩展。通过逐步手写这些内容，你将掌握 RPC 框架的设计思想、模块拆分、SPI 扩展机制，以及如何优雅地与主流框架集成，提升架构能力与编码水平。

- [手写 RPC 01：框架设计与核心概念](https://zpj80231.github.io/znote/views/source/code/rpc/rpc-source-01.html)
- [手写 RPC 02：快速实践与运行测试](https://zpj80231.github.io/znote/views/source/code/rpc/rpc-source-02.html)
- [手写 RPC 03：核心实现解析](https://zpj80231.github.io/znote/views/source/code/rpc/rpc-source-03.html)
- [手写 RPC 04：Spring 框架深度集成](https://zpj80231.github.io/znote/views/source/code/rpc/rpc-source-04.html)

| 分类              | 实现功能点        | 备注                                         |
|------------------|-----------------|----------------------------------------------|
| **基础定义**     | - 自定义传输协议<br>- 自定义编解码器<br>- 传输消息体<br>- 序列化方式（可扩展）<br>- 压缩方式（可扩展）<br>- 框架扩展性  | 支持多种协议和扩展，便于后续功能增强         |
| **服务端（服务暴露）** | - 自动扫描（基于注解）<br>- 自动注册（基于注解和注册中心，可扩展 nacos、zookeeper 等）<br>- 注册中心（默认本地注册，纯手写，模拟 zk 的文件路径方式）<br>- 分组和版本号（支持 API 分组、API 版本号，默认无分组、无版本号） | 方便服务自动发现与管理，支持多环境和版本     |
| **客户端（服务发现）** | - 自动发现（基于注册中心、API 分组、API 版本号）<br>- 负载均衡（随机、轮询，可扩展）<br>- 连接复用（每个服务端只需复用一个连接）<br>- 服务代理（客户端调用远程方法如同本地方法）<br>- 远程调用（通过自定义 rpc 进行远程调用）<br>- 多实例启动（可同时启动多个服务端，模拟多实例场景）<br>- 优雅关闭（无可用 rpc 连接时自动关闭服务） | 提升调用效率，支持高可用和多实例部署         |
| **SPI 扩展**     | - 序列化（基于 SPI 实现动态扩展）<br>- 负载均衡（基于 SPI 实现动态扩展）<br>- 服务注册（基于 SPI 实现动态扩展）<br>- 服务发现（基于 SPI 实现动态扩展）<br>- 压缩算法（基于 SPI 实现动态扩展）                                 | 插拔式扩展，便于自定义和集成第三方实现       |
| **Spring 深度集成** | - `@EnableRpcClient`（开启 rpc 客户端，自动服务发现、远程调用）<br>- `@RpcReference`（类似 @Autowire，注入远程服务）<br>- `@EnableRpcService`（开启 rpc 服务端，自动服务暴露）<br>- `@RpcService`（标识一个真正的服务提供者） | 与 Spring 生态无缝集成，提升开发效率         |
