package com.springframework.context.annotation;

import cn.hutool.core.text.CharSequenceUtil;
import com.springframework.beans.factory.config.BeanDefinition;
import com.springframework.beans.factory.config.BeanDefinitionRegistry;
import com.springframework.stereotype.Component;

import java.util.Set;

/**
 * 类路径 BeanDefinition 扫描器
 *
 * @author zhangpengjun
 * @date 2023/9/13
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * 扫描指定包下的所有文件，判断是否含有指定注解，将符合规则的文件转换为 BeanDefinition 加入到容器中
     *
     * @param basePackages 基本程序包
     */
    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidates) {
                // 解析 Bean 的作用域和属性
                String scope = resolveBeanScope(beanDefinition);
                if (CharSequenceUtil.isNotBlank(scope)) {
                    beanDefinition.setScope(scope);
                }
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }
    }

    /**
     * 解析 bean 作用域
     *
     * @param beanDefinition bean定义
     * @return {@link String}
     */
    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null && CharSequenceUtil.isNotBlank(scope.value())) {
            return scope.value();
        }
        return "";
    }

    /**
     * 确定 bean 名称
     *
     * @param beanDefinition bean定义
     * @return {@link String}
     */
    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        if (component != null && CharSequenceUtil.isNotBlank(component.value())) {
            return component.value();
        }
        return CharSequenceUtil.lowerFirst(beanClass.getSimpleName());
    }

}
