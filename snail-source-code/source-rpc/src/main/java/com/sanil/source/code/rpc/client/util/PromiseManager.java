package com.sanil.source.code.rpc.client.util;

import io.netty.util.concurrent.Promise;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PromiseManager {

    private static final Map<Long, Promise<Object>> PROMISE_MAP = new ConcurrentHashMap<>();

    public static void add(long requestId, Promise<Object> promise) {
        PROMISE_MAP.put(requestId, promise);
    }

    public static Promise<Object> remove(long requestId) {
        return PROMISE_MAP.remove(requestId);
    }

}
