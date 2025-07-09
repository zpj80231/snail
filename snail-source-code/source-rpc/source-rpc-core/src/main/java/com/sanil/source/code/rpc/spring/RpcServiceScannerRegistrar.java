package com.sanil.source.code.rpc.spring;

import com.sanil.source.code.rpc.server.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RPC 服务扫描器注册器
 *
 * @author zhangpj
 * @date 2025/7/7
 */
@Slf4j
public class RpcServiceScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取启动注解的属性
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableRpcService.class.getName()));
        if (attributes == null) {
            log.warn("annotation not found on {}", importingClassMetadata.getClassName());
            return;
        }

        // 解析扫描包路径
        List<String> basePackages = getBasePackages(importingClassMetadata, attributes);
        if (basePackages.isEmpty()) {
            log.warn("No base packages specified for RPC service scanning");
            return;
        }

        // 创建并配置扫描器
        ClassPathBeanDefinitionScanner rpcServiceScanner = new ClassPathBeanDefinitionScanner(registry);
        rpcServiceScanner.addIncludeFilter(new AnnotationTypeFilter(RpcService.class));
        rpcServiceScanner.setResourceLoader(resourceLoader);

        // 执行扫描
        String[] packagesToScan = basePackages.toArray(new String[0]);
        int rpcServiceBeanCount = rpcServiceScanner.scan(packagesToScan);
        log.info("RPC service scan completed. basePackages:{}, rpcServiceBeanCount:{}", basePackages, rpcServiceBeanCount);
    }

    /**
     * 获取基础扫描包路径
     *
     * @param importingClassMetadata 导入类的元数据
     * @param attributes             注解属性
     * @return 基础包路径列表
     */
    private List<String> getBasePackages(AnnotationMetadata importingClassMetadata,
                                         AnnotationAttributes attributes) {
        List<String> basePackages = new ArrayList<>();

        // 从 value 属性获取
        String[] valuePackages = attributes.getStringArray("value");
        if (valuePackages.length > 0) {
            basePackages.addAll(Arrays.asList(valuePackages));
        }
        // 从 basePackages 属性获取
        String[] basePackageArray = attributes.getStringArray("basePackages");
        if (basePackageArray.length > 0) {
            basePackages.addAll(Arrays.asList(basePackageArray));
        }
        // 从 basePackageClasses 获取
        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
        for (Class<?> basePackageClass : basePackageClasses) {
            basePackages.add(ClassUtils.getPackageName(basePackageClass));
        }
        // 如果没有指定任何包，使用标注注解的类所在的包
        if (basePackages.isEmpty()) {
            String defaultPackage = ClassUtils.getPackageName(importingClassMetadata.getClassName());
            if (StringUtils.hasText(defaultPackage)) {
                basePackages.add(defaultPackage);
            }
        }

        // 去重并过滤空值
        return basePackages.stream()
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

}
