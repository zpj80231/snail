package com.sanil.source.code.rpc.client.proxy;

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
public class RpcReferenceBuilder {

    /**
     * 分组
     */
    private String group;
    /**
     * 版本
     */
    private String version;
    /**
     * service
     */
    private Class<?> service;

}
