package com.snail.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.PropertyValue;
import com.snail.springframework.beans.factory.config.BeanDefinition;
import com.snail.springframework.beans.factory.config.BeanDefinitionRegistry;
import com.snail.springframework.beans.factory.config.BeanReference;
import com.snail.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import com.snail.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import com.snail.springframework.core.io.Resource;
import com.snail.springframework.core.io.ResourceLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 从 xml 文件读取 bean，转换为 BeanDefinition，并注册到 BeanDefinitionRegistry
 *
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try (InputStream inputStream = resource.getInputStream()) {
            doLoadBeanDefinitions(inputStream);
        } catch (IOException | ClassNotFoundException | DocumentException e) {
            throw new BeansException("Failed parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    private void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();

        // 解析 component-scan 标签，从指定包下扫描类文件并加载为 BeanDefinition 对象。这里包含了属性表达式值的替换操作。
        Element componentScan = root.element("component-scan");
        if (componentScan != null) {
            String scanPath = componentScan.attributeValue("base-package");
            if (StrUtil.isBlank(scanPath)) {
                throw new BeansException("The value of base-package attribute can not be empty or null");
            }
            scanPackage(scanPath);
        }

        // 解析 <bean> 标签，从 xml 配置文件中加载为 BeanDefinition
        List<Element> beanList = root.elements("bean");
        for (Element bean : beanList) {
            // 1. 解析 bean 标签
            String id = bean.attributeValue("id");
            String name = bean.attributeValue("name");
            String className = bean.attributeValue("class");
            String initMethod = bean.attributeValue("init-method");
            String destroyMethod = bean.attributeValue("destroy-method");
            String beanScope = bean.attributeValue("scope");

            // id 和 name 同时存在的情况下，id 优先级高于 name
            String beanName = StrUtil.isNotBlank(id) ? id : name;
            // 反射获取Class
            Class<?> clazz = Class.forName(className);
            if (StrUtil.isBlank(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            // 2. bean 解析后 -> 转为 BeanDefinition
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethod);
            beanDefinition.setDestroyMethodName(destroyMethod);
            if (StrUtil.isNotBlank(beanScope)) {
                beanDefinition.setScope(beanScope);
            }

            // 3. 解析属性，赋值给 BeanDefinition
            List<Element> propertyList = bean.elements("property");
            for (Element property : propertyList) {
                // 解析 property 标签
                String attrName = property.attributeValue("name");
                String attrValue = property.attributeValue("value");
                String attrRef = property.attributeValue("ref");
                // 解析 对象 引用
                Object value = StrUtil.isNotBlank(attrRef) ? new BeanReference(attrRef) : attrValue;
                // 4. bean 属性解析后 -> 赋值给 BeanDefinition
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }

            // 不允许有重复的 bean 被定义
            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate bean: [" + beanName + "] is not allowed to be defined");
            }

            // 5. 注册 BeanDefinition
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }

    /**
     * 从指定包下，扫描注解类，组装 BeanDefinition 加入到容器中
     *
     * @param scanPath 扫描路径
     */
    private void scanPackage(String scanPath) {
        String[] basePackages = StrUtil.splitToArray(scanPath, ",");
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackages);
    }

}
