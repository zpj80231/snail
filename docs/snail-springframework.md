Spring IOC 流程

对 Spring 中的核心类：DefaultListableBeanFactory 做一个简单初步的实现：

定义一些职责和能力：

1. BeanFactory：提供获取 Bean 的能力；

2. BeanDefinition：Spring 可以通过 xml 或 注解 的方式加载一个 Bean，但为了更好的扩展，不管从何处加载的 Bean， 最好都统一转换为一个指定对象，
   那么就需要定义一个统一的门面对象：BeanDefinition，只要有 BeanDefinition 我们就可以实例化一个 Bean；

3. SingletonBeanRegistry：提供获取一个 单例Bean 的能力（其实就是通过每个BeanDefinition定义的Class反射获得一个Bean，并缓存起来）；

4. BeanDefinitionRegistry：提供注册 BeanDefinition 的能力；

具体的实现：

1. DefaultSingletonBeanRegistry： implements SingletonBeanRegistry，
   实现了 单例Bean 的添加，获取；

2. AbstractBeanFactory：extends DefaultSingletonBeanRegistry implements BeanFactory，
   实现了 getBean(String beanName)
   而 getBean(String beanName) 的实现则主要是调用留给子类实现的空方法 createBean(beanName, beanDefinition)；

3. AbstractAutowireCapableBeanFactory：extends AbstractBeanFactory，实现createBean(beanName, beanDefinition)，
   实现通过 BeanDefinition 创建 Bean 的能力；

4. ★ DefaultListableBeanFactory：extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry，
   实现注册 BeanDefinition 的能力，获得一个 Bean 的能力；
   ```java
   public void testDefaultListableBeanFactory() {
      // DefaultListableBeanFactory 的用法
      DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
      // 1. 注册Bean的能力：将一个普通对象转换为 BeanDefinition，并注册进容器中
      BeanDefinition beanDefinition = new BeanDefinition(Cat.class);
      beanFactory.registerBeanDefinition("cat", beanDefinition);
      // 2. 获取Bean的能力：从容器中获取指定 Bean，第一次获取会示例化并缓存
      Cat cat = (Cat) beanFactory.getBean("cat");
      cat.name();
      // 3. 第二次会直接从缓存中获取
      Cat cat_cache = (Cat) beanFactory.getBean("cat");
      cat_cache.name();
   }
   ```

