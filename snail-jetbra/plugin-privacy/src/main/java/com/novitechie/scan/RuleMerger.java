package com.novitechie.scan;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RuleMerger {

    private static final String MARKER_RESOURCE_RULE = "/6c81ec87e55d331c267262e892427a3d93d76683.txt";

    private RuleMerger() {
    }

    public static List<FilterRule> merge(List<FilterRule> first, List<FilterRule> second) {
        List<FilterRule> merged = new ArrayList<>();
        addRules(merged, first);
        addRules(merged, second);
        return merged;
    }

    public static List<FilterRule> toResourceRules(List<FilterRule> classRules) {
        List<FilterRule> resourceRules = new ArrayList<>();
        if (classRules == null) {
            return resourceRules;
        }
        for (FilterRule classRule : classRules) {
            if (classRule == null || classRule.getRule() == null) {
                continue;
            }
            String resourcePath = "/" + classRule.getRule().replace('.', '/') + ".class";
            addRule(resourceRules, FilterRule.of("EQUAL", resourcePath));
        }
        return resourceRules;
    }

    public static List<FilterRule> adjustMarkerResourceRule(List<FilterRule> hideResourceRules,
                                                            boolean markerResourceExists) {
        List<FilterRule> adjusted = new ArrayList<>();
        addRules(adjusted, hideResourceRules);
        FilterRule markerRule = FilterRule.of("EQUAL", MARKER_RESOURCE_RULE);
        if (markerResourceExists) {
            removeRule(adjusted, markerRule);
            DebugInfo.output("[PRIVACY-SCAN] Marker resource exists, removed Hide_Resource rule: "
                    + MARKER_RESOURCE_RULE);
            return adjusted;
        }
        addRule(adjusted, markerRule);
        DebugInfo.output("[PRIVACY-SCAN] Marker resource not found, added Hide_Resource rule: "
                + MARKER_RESOURCE_RULE);
        return adjusted;
    }

    private static void addRules(List<FilterRule> target, List<FilterRule> rules) {
        if (rules == null) {
            return;
        }
        for (FilterRule rule : rules) {
            addRule(target, rule);
        }
    }

    private static void addRule(List<FilterRule> target, FilterRule rule) {
        if (rule == null) {
            return;
        }
        for (FilterRule existing : target) {
            if (sameRule(existing, rule)) {
                return;
            }
        }
        target.add(rule);
    }

    private static void removeRule(List<FilterRule> target, FilterRule rule) {
        if (target == null || rule == null) {
            return;
        }
        for (int i = target.size() - 1; i >= 0; i--) {
            if (sameRule(target.get(i), rule)) {
                target.remove(i);
            }
        }
    }

    private static boolean sameRule(FilterRule left, FilterRule right) {
        return Objects.equals(left.getType(), right.getType()) && Objects.equals(left.getRule(), right.getRule());
    }
}
