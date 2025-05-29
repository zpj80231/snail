package com.sanil.source.code.rpc.core.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangpj
 * @date 2025/5/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcServiceConfig {

    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 群
     */
    private String group;
    /**
     * 版本
     */
    private String version;
    /**
     * 服务
     */
    private Object service;

}
