package com.novitechie;

import com.janetfilter.core.commons.DebugInfo;

/**
 * @author YeloChick
 */
public class LogUtil {

    public static void printStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        DebugInfo.output("===========================stackTrace: ");
        for (StackTraceElement stackTraceElement : stackTrace) {
            DebugInfo.output(stackTraceElement.getClassName() + ":" + stackTraceElement.getMethodName());
        }
    }
}
