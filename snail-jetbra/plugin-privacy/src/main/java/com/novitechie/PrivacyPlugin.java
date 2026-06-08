package com.novitechie;

import com.janetfilter.core.Environment;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import com.janetfilter.core.plugin.PluginConfig;
import com.janetfilter.core.plugin.PluginEntry;
import com.novitechie.rules.IdeaPluginRule;
import com.novitechie.rules.LoadClassRule;
import com.novitechie.rules.ResourceRule;
import com.novitechie.rules.StackTraceRule;
import com.novitechie.rules.SystemRule;
import com.novitechie.scan.CanaryAutoScanner;
import com.novitechie.scan.RuleMerger;

import java.util.Arrays;
import java.util.List;

public class PrivacyPlugin implements PluginEntry {

    @Override
    public void init(Environment environment, PluginConfig config) {
        List<FilterRule> autoScanClassRules = CanaryAutoScanner.scan(config);
        List<FilterRule> autoScanResourceRules = RuleMerger.toResourceRules(autoScanClassRules);
        List<FilterRule> ignoreClassRules = RuleMerger.merge(config.getBySection("Ignore_Class"), autoScanClassRules);
        List<FilterRule> ignoreResourceRules = RuleMerger.merge(config.getBySection("Ignore_Resource"), autoScanResourceRules);

        IdeaPluginRule.initRules(config.getBySection("Hide_Plugin"));
        LoadClassRule.initRules(config.getBySection("Hide_Package"), ignoreClassRules);
        ResourceRule.initRules(config.getBySection("Hide_Resource"), ignoreResourceRules);
        SystemRule.initRules(config.getBySection("Hide_Env"), config.getBySection("Hide_Property"));
        StackTraceRule.initRules(config.getBySection("Trace_Check_Package"));
    }

    @Override
    public String getName() {
        return "PRIVACY";
    }

    @Override
    public String getAuthor() {
        return "novice.li";
    }

    @Override
    public List<MyTransformer> getTransformers() {
        return Arrays.asList(
                // new CollectionsTransformer(),
                new VMOptionsTransformer(),
                new FilesTransformer(),
                new FileInputStreamTransformer(),
                new PluginClassLoaderTransformer(),
                new LicensingFacadeTransformer(),
                new PluginManagerCoreTransformer(),
                new RuntimeMXBeanTransformer(),
                new SystemTransformer(),
                new ClassTransformer(),
                new ClassLoaderTransformer(),
                new MethodTransformer());
    }
}
