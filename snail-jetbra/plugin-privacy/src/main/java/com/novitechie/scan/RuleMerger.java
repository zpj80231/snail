package com.novitechie.scan;

import com.janetfilter.core.models.FilterRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RuleMerger {

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

    private static boolean sameRule(FilterRule left, FilterRule right) {
        return Objects.equals(left.getType(), right.getType()) && Objects.equals(left.getRule(), right.getRule());
    }
}
