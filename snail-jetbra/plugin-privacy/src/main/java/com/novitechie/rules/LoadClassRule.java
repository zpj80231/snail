package com.novitechie.rules;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;
import com.novitechie.LogUtil;

import java.util.Collections;
import java.util.List;

public class LoadClassRule {

    private static List<FilterRule> hidePackageRules = Collections.emptyList();

    private static List<FilterRule> ignoreClassRules = Collections.emptyList();

    public static void initRules(List<FilterRule> hidePackageRules, List<FilterRule> ignoreClassRules) {
        LoadClassRule.hidePackageRules = hidePackageRules;
        LoadClassRule.ignoreClassRules = ignoreClassRules;
    }

    public static void check(String name) throws Exception {
        if (!checkIgnoreClass(name) && checkHidePackage(name)) {
            DebugInfo.output("======================Hide Class: " + name);
            LogUtil.printStackTrace();
            throw new ClassNotFoundException(name);
        }
    }

    private static boolean checkHidePackage(String name) {
        return hidePackageRules.stream().anyMatch(rule -> rule.test(name));
    }

    private static boolean checkIgnoreClass(String name) {
        return ignoreClassRules.stream().anyMatch(rule -> rule.test(name));
    }

}
