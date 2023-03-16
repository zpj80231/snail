# Spring IOC 源码

## 核心容器 DefaultListableBeanFactory

对 Spring 中的核心类：DefaultListableBeanFactory 做一个简单初步的实现：

定义一些职责和能力：

1. BeanFactory：提供获取 Bean 的能力；

2. BeanDefinition：Spring 可以通过 xml 或 注解 的方式加载一个 Bean，但为了更好的扩展，不管从何处加载的 Bean， 最好都统一转换为一个指定对象，
   那么就需要定义一个统一的门面对象：BeanDefinition，只要有 BeanDefinition 我们就可以实例化一个 Bean；

3. SingletonBeanRegistry：提供获取一个 单例Bean 的能力（其实就是通过每个BeanDefinition定义的Class反射获得一个Bean，并缓存起来）；

4. BeanDefinitionRegistry：提供注册 BeanDefinition 的能力；

具体的实现：

1. DefaultSingletonBeanRegistry： implements SingletonBeanRegistry， 实现了 单例Bean 的添加，获取；

2. AbstractBeanFactory：extends DefaultSingletonBeanRegistry implements BeanFactory， 实现了 getBean(String beanName)
   而 getBean(String beanName) 的实现则主要是调用留给子类实现的空方法 createBean(beanName, beanDefinition)；

3. AbstractAutowireCapableBeanFactory：extends AbstractBeanFactory，实现createBean(beanName, beanDefinition)， 实现通过
   BeanDefinition 创建 Bean 的能力；

4. ★ DefaultListableBeanFactory：extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry， 实现注册
   BeanDefinition 的能力，获得一个 Bean 的能力；

测试：

```java
public void testDefaultListableBeanFactory(){
        // DefaultListableBeanFactory 的用法
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
        // 1. 注册Bean的能力：将一个普通对象转换为 BeanDefinition，并注册进容器中
        BeanDefinition beanDefinition=new BeanDefinition(Cat.class);
        beanFactory.registerBeanDefinition("cat",beanDefinition);
        // 2. 获取Bean的能力：从容器中获取指定 Bean，第一次获取会示例化并缓存
        Cat cat=(Cat)beanFactory.getBean("cat");
        cat.name();
        // 3. 第二次会直接从缓存中获取
        Cat cat_cache=(Cat)beanFactory.getBean("cat");
        cat_cache.name();
        }
```

## 实例化策略 InstantiationStrategy

要想容器支持对 Bean的有参构造器 实例化的方式，需要定义一个实例化策略接口 InstantiationStrategy。

实例化方法：Object instantiatie(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args);

一个 Bean 的实例化，需要的基本参数有：

- BeanDefinition：主要保存了一个具体的类，主要就是实例化这个类
- Constructor：通过哪个构造方法实例化
- args：构造方法的参数

当然，定义这个 InstantiationStrategy 策略接口也是为了支持不同实例化方式方便扩展。

- SimpleInstantiationStrategy：JDK 实例化策略

- CglibSubclassingInstantiationStrategy：cglib 实例化策略

AbstractAutowireCapableBeanFactory：修改 createBean(String beanName, BeanDefinition beanDefinition) 实现，加入实例化策略。

测试：

```java
@Test
public void testDefaultListableBeanFactoryGetBeanWithConstructor(){
        // DefaultListableBeanFactory 的用法
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
        // 1. 注册Bean的能力：将一个普通对象转换为 BeanDefinition，并注册进容器中
        BeanDefinition beanDefinition=new BeanDefinition(Cat.class);
        beanFactory.registerBeanDefinition("cat",beanDefinition);
        // 2. 获取Bean的能力：从容器中获取指定 Bean，第一次获取会示例化并缓存
        Cat cat=(Cat)beanFactory.getBean("cat","Cat -> Constructor");
        cat.printName();
        // 3. 第二次会直接从缓存中获取
        Cat cat_cache=(Cat)beanFactory.getBean("cat");
        cat_cache.printName();
        }
```

## 属性填充

PropertyValue：将一个 Bean 本身的 字段名、字段值映射为一个 PropertyValue；

PropertyValues：当一个 Bean 有多个字段时，将 PropertyValue 保存为一个集合；

BeanDefinition：之前只保存了实例化的类型Class（实例化用），现在将PropertyValues也填充进去（属性填充用）；

BeanReference：属性填充时可能遇到 Bean 的依赖，A 依赖 B, B 依赖 C, 循环调用实例化，主要解决 Bean依赖 问题的属性填充；

AbstractAutowireCapableBeanFactory：修改 createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args)
加入属性填充步骤；

测试：

```java
 @Test
public void testDefaultListableBeanFactoryGetBeanWithApplyPropertyValues(){
        // DefaultListableBeanFactory 的用法
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();

        // 1. 注册Bean的能力：将一个普通对象（和属性）转换为 BeanDefinition，并注册进容器中
        // 注册一个 cat
        PropertyValues catPropertyValues=new PropertyValues();
        catPropertyValues.addPropertyValue(new PropertyValue("name","TomCat"));
        BeanDefinition catBeanDefinition=new BeanDefinition(Cat.class,catPropertyValues);
        beanFactory.registerBeanDefinition("cat",catBeanDefinition);
        // 注册一个 dog，dog 依赖 cat
        PropertyValues dogPropertyValues=new PropertyValues();
        dogPropertyValues.addPropertyValue(new PropertyValue("name","JjDog"));
        dogPropertyValues.addPropertyValue(new PropertyValue("cat",new BeanReference("cat")));
        BeanDefinition dogBeanDefinition=new BeanDefinition(Dog.class,dogPropertyValues);
        beanFactory.registerBeanDefinition("dog",dogBeanDefinition);

        // 2. 获取Bean的能力：从容器中获取指定 Bean，第一次获取会示例化并缓存
        Dog dog=(Dog)beanFactory.getBean("dog");
        dog.printName();
        }
```



