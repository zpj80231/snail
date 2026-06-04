package com.novitechie.scan;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.enums.RuleType;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.PluginConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CanaryAutoScanner {

    private static final List<FilterRule> DEFAULT_SCAN_PLUGINS = Arrays.asList(
            new FilterRule(RuleType.EQUAL, "izhangzhihao.rainbow.brackets"));
    private static final List<FilterRule> DEFAULT_SCAN_PACKAGES = Arrays.asList(
            new FilterRule(RuleType.PREFIX, "com.janetfilter."),
            new FilterRule(RuleType.PREFIX, "com.novitechie."));

    private CanaryAutoScanner() {
    }

    public static List<FilterRule> scan(PluginConfig config) {
        List<FilterRule> pluginRules = rules(config, "Auto_Scan_Plugin", DEFAULT_SCAN_PLUGINS);
        List<FilterRule> packageRules = rules(config, "Auto_Scan_Package", DEFAULT_SCAN_PACKAGES);
        List<FilterRule> excludeRules = emptyIfNull(config.getBySection("Auto_Scan_Exclude"));

        if (pluginRules.isEmpty() || packageRules.isEmpty()) {
            DebugInfo.warn("[PRIVACY-SCAN] Auto scan disabled: plugin or package rules are empty");
            return Collections.emptyList();
        }

        List<File> scanTargets = JbDirectoryScanner.getPluginDir();
        if (scanTargets.isEmpty()) {
            DebugInfo.warn("[PRIVACY-SCAN] No plugin scan targets found");
            return Collections.emptyList();
        }

        DebugInfo.output("[PRIVACY-SCAN] Plugin targets: " + fileSummary(scanTargets));
        DebugInfo.output("[PRIVACY-SCAN] Scan_Plugin: " + ruleSummary(pluginRules));
        DebugInfo.output("[PRIVACY-SCAN] Scan_Package: " + ruleSummary(packageRules));
        DebugInfo.output("[PRIVACY-SCAN] Exclude_Package: " + ruleSummary(excludeRules));

        CanaryScanner.ScanResult result = CanaryScanner.scan(scanTargets, pluginRules, packageRules, excludeRules);
        List<String> classNames = result.getClassNames();
        List<FilterRule> classRules = new ArrayList<>(classNames.size());
        DebugInfo.output("[PRIVACY-SCAN] Found " + classNames.size() + " canary classes");
        for (String className : classNames) {
            classRules.add(FilterRule.of("EQUAL", className));
            DebugInfo.output("[PRIVACY-SCAN]   - " + className);
        }
        return classRules;
    }

    private static List<FilterRule> rules(PluginConfig config, String section, List<FilterRule> defaultValue) {
        List<FilterRule> rules = config.getBySection(section);
        if (hasSection(config, section)) {
            return emptyIfNull(rules);
        }
        if (defaultValue == null) {
            return Collections.emptyList();
        }
        return defaultValue;
    }

    private static boolean hasSection(PluginConfig config, String section) {
        return config.getData() != null && config.getData().containsKey(section);
    }

    private static List<FilterRule> emptyIfNull(List<FilterRule> rules) {
        return rules == null ? Collections.emptyList() : rules;
    }

    private static String fileSummary(List<File> files) {
        StringBuilder sb = new StringBuilder();
        for (File file : files) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(file.getAbsolutePath());
        }
        return sb.toString();
    }

    private static String ruleSummary(List<FilterRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return "(none)";
        }
        StringBuilder sb = new StringBuilder();
        for (FilterRule rule : rules) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(rule);
        }
        return sb.toString();
    }
}
