package com.snail.springframework.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.snail.springframework.beans.factory.config.BeanDefinition;
import com.snail.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 类路径扫描候选组件提供程序
 *
 * @author zhangpengjun
 * @date 2023/9/13
 */
public class ClassPathScanningCandidateComponentProvider {

    /**
     * 查找 @Component 候选组件
     *
     * @param basePackage 基本包
     * @return {@link Set}<{@link BeanDefinition}>
     */
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        LinkedHashSet<BeanDefinition> candidates = new LinkedHashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }
        return candidates;
    }

}
