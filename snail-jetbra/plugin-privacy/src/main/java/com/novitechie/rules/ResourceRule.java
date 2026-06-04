package com.novitechie.rules;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;
import com.novitechie.LogUtil;

import java.util.Collections;
import java.util.List;

/**
 * @author YeloChick
 */
public class ResourceRule {

    private static List<FilterRule> hideResourceRules = Collections.emptyList();

    private static List<FilterRule> ignoreResourceRules = Collections.emptyList();

    public static void initRules(List<FilterRule> hideResourceRules, List<FilterRule> ignoreResourceRules) {
        ResourceRule.hideResourceRules = hideResourceRules;
        ResourceRule.ignoreResourceRules = ignoreResourceRules;
    }

    public static boolean check(String name) {
        if (!checkIgnoreResource(name) && checkHideResource(name) && StackTraceRule.check()) {
            DebugInfo.output("======================Hide Resource: " + name);
            LogUtil.printStackTrace();
            return true;
        }
        return false;
    }

    private static boolean checkHideResource(String name) {
        return hideResourceRules.stream().anyMatch(rule -> rule.test(name));
    }

    private static boolean checkIgnoreResource(String name) {
        return ignoreResourceRules.stream().anyMatch(rule -> rule.test(name));
    }
}
