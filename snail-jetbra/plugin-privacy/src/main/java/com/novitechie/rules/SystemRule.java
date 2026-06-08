package com.novitechie.rules;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;
import com.novitechie.LogUtil;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * @author YeloChick
 */
public class SystemRule {

    private static final String JB_VM_OPTIONS_FILE = "jb.vmOptionsFile";

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
        if ((checkVMOptionsProperty(name) || checkHideProperty(name)) && StackTraceRule.check()) {
            DebugInfo.output("======================Hide Property: " + name);
            LogUtil.printStackTrace();
            return true;
        }
        return false;
    }

    public static String hookProperty(String name) {
        if (checkVMOptionsProperty(name)) {
            Path path = VMOptionsRule.hook();
            return path == null ? null : path.toString();
        }
        return null;
    }

    private static boolean checkHideEnv(String name) {
        return hideEnvRules.stream().anyMatch(rule -> rule.test(name));
    }

    private static boolean checkHideProperty(String name) {
        return hidePropertyRules.stream().anyMatch(rule -> rule.test(name));
    }

    private static boolean checkVMOptionsProperty(String name) {
        return JB_VM_OPTIONS_FILE.equals(name);
    }
}
