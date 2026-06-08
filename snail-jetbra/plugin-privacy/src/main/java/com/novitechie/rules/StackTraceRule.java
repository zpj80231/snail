package com.novitechie.rules;

import com.janetfilter.core.commons.DebugInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class StackTraceRule {

    private static final Pattern PACKAGE_NAME_PATTERN = Pattern.compile("\\A\\p{ASCII}*\\z");

    public static boolean check() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            String methodName = stackTraceElement.getMethodName();
            if (!PACKAGE_NAME_PATTERN.matcher(methodName).matches()) {
                return true;
            }
            if (methodName.length() <= 2) {
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
