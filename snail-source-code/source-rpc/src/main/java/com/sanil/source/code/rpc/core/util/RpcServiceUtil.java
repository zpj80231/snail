package com.sanil.source.code.rpc.core.util;

import cn.hutool.core.util.StrUtil;
import lombok.NoArgsConstructor;

/**
 * @author zhangpj
 * @date 2025/5/29
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RpcServiceUtil {

    public static String getProviderName(String serviceName, String group, String version) {
        if (StrUtil.isAllBlank(group, version)) {
            return serviceName;
        }
        if (group == null) {
            group = "";
        }
        if (version == null) {
            version = "";
        }
        return StrUtil.join("@", serviceName, group, version);
    }

}
