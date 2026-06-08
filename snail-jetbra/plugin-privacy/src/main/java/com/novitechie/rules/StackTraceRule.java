package com.novitechie.rules;

import com.janetfilter.core.commons.DebugInfo;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class StackTraceRule {

    private static final Pattern PACKAGE_NAME_PATTERN = Pattern.compile("\\A\\p{ASCII}*\\z");
    private static final List<String> OFFICIAL_CLASS_PREFIXES = Arrays.asList(
            "java.",
            "javax.",
            "sun.",
            "com.sun.",
            "jdk.",
            "com.intellij.",
            "com.jetbrains.",
            "org.jetbrains.",
            "kotlin.");

    public static boolean check() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (isSuspiciousFrame(stackTraceElement)) {
                return true;
            }
        }
        return false;
    }

    static boolean isSuspiciousFrame(StackTraceElement stackTraceElement) {
        String methodName = stackTraceElement.getMethodName();
        if (!PACKAGE_NAME_PATTERN.matcher(methodName).matches()) {
            return true;
        }
        if (methodName.length() <= 1) {
            String className = stackTraceElement.getClassName();
            if (isOfficialClass(className)) {
                return false;
            }
            DebugInfo.info("short method frame, className: " + className + ", methodName: " + methodName);
            return true;
        }
        return false;
    }

    private static boolean isOfficialClass(String className) {
        if (className == null) {
            return false;
        }
        for (String prefix : OFFICIAL_CLASS_PREFIXES) {
            if (className.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public static Date hook() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (!PACKAGE_NAME_PATTERN.matcher(stackTraceElement.getMethodName()).matches()) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 60);
                DebugInfo.output("===========================getLicenseExpirationDate");
                return calendar.getTime();
            }
        }
        return null;
    }
}
