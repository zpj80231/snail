package com.novitechie.utils;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyUtil {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    public static String getProperty(Properties properties, String name) {
        String value = properties.getProperty(name);
        if (value == null) {
            return null;
        }
        return resolvePlaceholders(properties, value);
    }

    private static String resolvePlaceholders(Properties properties, String value) {
        Matcher matcher = PATTERN.matcher(value);
        StringBuffer resolved = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(1);
            String replacement = properties.getProperty(name);
            if (replacement == null) {
                matcher.appendReplacement(resolved, Matcher.quoteReplacement(matcher.group(0)));
                continue;
            }
            matcher.appendReplacement(resolved,
                    Matcher.quoteReplacement(resolvePlaceholders(properties, replacement)));
        }
        matcher.appendTail(resolved);
        return resolved.toString();
    }
}
