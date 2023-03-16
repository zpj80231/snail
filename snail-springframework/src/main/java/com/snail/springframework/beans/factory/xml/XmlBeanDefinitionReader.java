package com.snail.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.snail.springframework.beans.BeansException;
import com.snail.springframework.beans.PropertyValue;
import com.snail.springframework.beans.factory.config.BeanDefinition;
import com.snail.springframework.beans.factory.config.BeanDefinitionRegistry;
import com.snail.springframework.beans.factory.config.BeanReference;
import com.snail.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import com.snail.springframework.core.io.Resource;
import com.snail.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

/**
 * 从 xml 文件读取 bean，转换为 BeanDefinition，并注册到 BeanDefinitionRegistry
 *
 * @author zhangpengjun
 * @date 2023/3/16
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    protected XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try (InputStream inputStream = resource.getInputStream()) {
            doLoadBeanDefinitions(inputStream);
        } catch (IOException | ClassNotFoundException e) {
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

    private void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            // 过滤不符合规则的标签
            if (!(childNodes.item(i) instanceof Element)) {
                continue;
            }
            if (!"bean".equals(childNodes.item(i).getNodeName())) {
                continue;
            }

            // 1. 解析 bean 标签
            Element bean = (Element) childNodes.item(i);
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            // id 和 name 同时存在的情况下，id 优先级高于 name
            String beanName = StrUtil.isNotBlank(id) ? id : name;
            // 反射获取Class
            Class<?> clazz = Class.forName(className);
            if (StrUtil.isBlank(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            // 2. bean 解析后 -> 转为 BeanDefinition
            BeanDefinition beanDefinition = new BeanDefinition(clazz);

            // 3. 解析属性，赋值给 BeanDefinition
            for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                // 过滤不符合规则的标签
                if (!(bean.getChildNodes().item(j) instanceof Element)) {
                    continue;
                }
                if (!"property".equals(bean.getChildNodes().item(j).getNodeName())) {
                    continue;
                }
                // 解析 property 标签
                Element property = (Element) bean.getChildNodes().item(j);
                String attrName = property.getAttribute("name");
                String attrValue = property.getAttribute("value");
                String attrRef = property.getAttribute("ref");
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

}
