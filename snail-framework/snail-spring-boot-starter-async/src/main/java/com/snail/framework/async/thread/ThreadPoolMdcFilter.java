package com.snail.framework.async.thread;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.snail.framework.async.constant.LogConstant.TRACE_ID;

/**
 * 线程池 mdc filter
 *
 * @author zhangpengjun
 * @date 2022/09/22
 */
public class ThreadPoolMdcFilter {

    public static void setSleuthTraceId() {
        if (StrUtil.isNotBlank(MDC.get(TRACE_ID))) {
            MDC.put(TRACE_ID, MDC.get(TRACE_ID));
        } else {
            MDC.put(TRACE_ID, IdUtil.fastSimpleUUID());
        }
    }

    public static void setNewSleuthTraceId() {
        MDC.put(TRACE_ID, IdUtil.fastSimpleUUID());
    }

    public static void clearSleuthTraceId() {
        MDC.remove(TRACE_ID);
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setSleuthTraceId();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setSleuthTraceId();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

}
