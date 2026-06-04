package com.novitechie.rules;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;
import com.novitechie.LogUtil;

import java.util.Collections;
import java.util.List;

/**
 * @author YeloChick
 */
public class IdeaPluginRule {

    private static List<FilterRule> hidePluginRules = Collections.emptyList();

    public static void initRules(List<FilterRule> hidePluginRules) {
        IdeaPluginRule.hidePluginRules = hidePluginRules;
    }

    public static boolean check(String name) {
        if (checkHidePlugin(name)) {
            DebugInfo.output("======================Hide Plugin: " + name);
            LogUtil.printStackTrace();
            return true;
        }
        return false;
    }

    private static boolean checkHidePlugin(String name) {
        return hidePluginRules.stream().anyMatch(rule -> rule.test(name));
    }
}
