package com.novitechie.rules;

import com.janetfilter.core.commons.DebugInfo;
import com.novitechie.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YeloChick
 */
public class BootstrapClassRule {

    private static final List<String> PREVENT_LOAD_PACKAGES = new ArrayList<String>() {
        {
            add("com.janetfilter");
        }
    };

    public static boolean check(String name) throws Exception {
        boolean f = PREVENT_LOAD_PACKAGES.stream().anyMatch(name::startsWith);
        if (f) {
            DebugInfo.output("======================LoadBootstrapClass: " + name);
            LogUtil.printStackTrace();
            return true;
        }
        return false;
    }
}
