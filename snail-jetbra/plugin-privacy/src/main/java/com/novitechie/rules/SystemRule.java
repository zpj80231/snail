package com.novitechie.rules;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;
import com.novitechie.LogUtil;

import java.util.Collections;
import java.util.List;

/**
 * @author YeloChick
 */
public class SystemRule {

    private static List<FilterRule> hideEnvRules = Collections.emptyList();

    private static List<FilterRule> hidePropertyRules = Collections.emptyList();

    public static void initRules(List<FilterRule> hideEnvRules, List<FilterRule> hidePropertyRules) {
        SystemRule.hideEnvRules = hideEnvRules;
        SystemRule.hidePropertyRules = hidePropertyRules;
    }

    public static boolean checkEnv(String name) {
        if (checkHideEnv(name) && StackTraceRule.check()) {
            DebugInfo.output("======================Hide Env: " + name);
            LogUtil.printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean checkProperty(String name) {
        if (checkHideProperty(name) && StackTraceRule.check()) {
            DebugInfo.output("======================Hide Property: " + name);
            LogUtil.printStackTrace();
            return true;
        }
        return false;
    }

    private static boolean checkHideEnv(String name) {
        return hideEnvRules.stream().anyMatch(rule -> rule.test(name));
    }

    private static boolean checkHideProperty(String name) {
        return hidePropertyRules.stream().anyMatch(rule -> rule.test(name));
    }
}
