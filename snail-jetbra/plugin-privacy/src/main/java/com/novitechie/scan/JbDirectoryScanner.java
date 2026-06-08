package com.novitechie.scan;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.PluginConfig;
import com.novitechie.utils.PropertyUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JbDirectoryScanner {

    private static final String PATH_MANAGER_CLASS = "com.intellij.openapi.application.PathManager";
    private static final String IDEA_PATHS_SELECTOR_PROPERTY = "idea.paths.selector";
    private static final String IDEA_PLUGIN_PATH_SECTION = "Auto_Scan_Ide_Plugin_Path";
    private static final String IDEA_PROPERTIES_ENV_VARIABLE = "IDEA_PROPERTIES";
    private static final String IDEA_PROPERTIES_FILE_PATH = "bin/idea.properties";
    private static final String IDEA_PLUGINS_PATH_PROPERTY = "idea.plugins.path";
    private static final String ZIP_SUFFIX = ".zip";

    private JbDirectoryScanner() {
    }

    public static List<File> getPluginDir(PluginConfig config) {
        Class<?> pathManagerClass = loadClass(PATH_MANAGER_CLASS);
        List<FilterRule> configuredPathRules = config == null ? null : config.getBySection(IDEA_PLUGIN_PATH_SECTION);
        if (configuredPathRules == null || configuredPathRules.isEmpty()) {
            DebugInfo.warn("[PRIVACY-SCAN] No configured IDEA plugin path rules found, fallback to PathManager");
            return getPathManagerPluginDirs(pathManagerClass);
        }

        String selector = resolveCurrentSelector(pathManagerClass);
        if (isBlank(selector)) {
            DebugInfo.warn("[PRIVACY-SCAN] Current IDEA selector not found");
            return Collections.emptyList();
        }

        DebugInfo.output("[PRIVACY-SCAN] Current IDEA selector: " + selector);
        Set<File> pluginDirs = new LinkedHashSet<>();
        for (FilterRule rule : configuredPathRules) {
            addConfiguredPluginDir(pluginDirs, selector, rule);
        }
        if (pluginDirs.isEmpty()) {
            DebugInfo.warn("[PRIVACY-SCAN] No configured IDEA plugin path matched: " + selector);
            return Collections.emptyList();
        }

        DebugInfo.output("[PRIVACY-SCAN] Configured plugin roots: " + pluginDirs);
        return new ArrayList<>(pluginDirs);
    }

    public static List<File> getPluginDir() {
        return getPathManagerPluginDirs(loadClass(PATH_MANAGER_CLASS));
    }

    private static List<File> getPathManagerPluginDirs(Class<?> pathManagerClass) {
        if (pathManagerClass == null) {
            DebugInfo.warn("[PRIVACY-SCAN] PathManager not found");
            return Collections.emptyList();
        }

        Set<File> pluginDirs = new LinkedHashSet<>();
        addDirFromIdeaProperties(pathManagerClass, pluginDirs);
        if (pluginDirs.isEmpty()) {
            addDir(pluginDirs, invokePathManagerDir(pathManagerClass, "getPluginsDir"));
            addDir(pluginDirs, invokePathManagerDir(pathManagerClass, "getPluginsPath"));
        }

        List<File> targets = new ArrayList<>(pluginDirs);
        // 升级后首次启动时，IDE 已把新版下载到缓存目录，但要等 action.script 在更晚的时机
        // 才覆盖到 plugins 目录；Java Agent 早于 action.script 执行，只看 plugins 目录会拿到旧插件。
        // 缓存目录可能有数十个、累计上 GB 的 zip，逐个解压会显著拖慢启动，因此只挑出
        // 「有同名已安装目录、且 zip 比该目录新」的待应用更新包（仅比较 mtime，不解压）。
        List<File> pendingZips = selectPendingUpdateZips(resolveStagingDir(pathManagerClass), pluginDirs);
        targets.addAll(pendingZips);

        DebugInfo.output("[PRIVACY-SCAN] Plugin dirs: " + pluginDirs);
        DebugInfo.output("[PRIVACY-SCAN] Pending update zips: " + pendingZips);
        return targets;
    }

    private static void addDirFromIdeaProperties(Class<?> pathManagerClass, Set<File> pluginDirs) {
        File ideaPropertiesFile;
        String configuredPath = System.getenv(IDEA_PROPERTIES_ENV_VARIABLE);
        if (isBlank(configuredPath)) {
            File homePath = invokePathManagerDir(pathManagerClass, "getHomePath");
            DebugInfo.output("[PRIVACY-SCAN] PathManager home path: " + homePath);
            if (homePath == null) {
                return;
            }
            ideaPropertiesFile = new File(homePath, IDEA_PROPERTIES_FILE_PATH);
        } else {
            DebugInfo.output("[PRIVACY-SCAN] IDEA_PROPERTIES environment variable: " + configuredPath);
            ideaPropertiesFile = new File(configuredPath);
        }

        if (!ideaPropertiesFile.exists()) {
            return;
        }

        DebugInfo.output("[PRIVACY-SCAN] idea.properties file path: " + ideaPropertiesFile);
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(ideaPropertiesFile.toPath())) {
            properties.load(input);
            String pluginsPath = PropertyUtil.getProperty(properties, IDEA_PLUGINS_PATH_PROPERTY);
            DebugInfo.output("[PRIVACY-SCAN] IDEA plugins path property: " + pluginsPath);
            if (isBlank(pluginsPath)) {
                DebugInfo.warn("[PRIVACY-SCAN] IDEA plugins path not found in idea.properties");
                return;
            }
            addDir(pluginDirs, new File(pluginsPath));
        } catch (IOException e) {
            DebugInfo.warn("[PRIVACY-SCAN] Loading idea.properties failed", e);
        }
    }

    private static void addConfiguredPluginDir(Set<File> pluginDirs, String selector, FilterRule rule) {
        if (rule == null || isBlank(rule.getRule())) {
            return;
        }
        String value = rule.getRule().trim();
        int separator = value.indexOf('=');
        if (separator <= 0 || separator >= value.length() - 1) {
            DebugInfo.warn("[PRIVACY-SCAN] Invalid IDEA plugin path rule: " + value);
            return;
        }

        String selectorRule = value.substring(0, separator).trim();
        String path = value.substring(separator + 1).trim();
        FilterRule selectorFilter = new FilterRule(rule.getType(), selectorRule);
        if (!selectorFilter.test(selector)) {
            return;
        }

        File dir = new File(stripSurroundingQuotes(path));
        if (!dir.isDirectory()) {
            DebugInfo.warn("[PRIVACY-SCAN] Configured plugin path ignored, directory not found: "
                    + dir.getAbsolutePath());
            return;
        }

        addDir(pluginDirs, dir);
        DebugInfo.output("[PRIVACY-SCAN] Configured plugin path matched: " + selectorRule + " -> "
                + dir.getAbsolutePath());
    }

    private static String resolveCurrentSelector(Class<?> pathManagerClass) {
        String selector = getProperty(IDEA_PATHS_SELECTOR_PROPERTY);
        if (!isBlank(selector)) {
            return selector;
        }
        return invokePathManagerString(pathManagerClass, "getPathsSelector");
    }

    private static File resolveStagingDir(Class<?> pathManagerClass) {
        File tempPath = invokePathManagerDir(pathManagerClass, "getPluginTempPath");
        if (tempPath != null) {
            return tempPath;
        }
        File systemPath = invokePathManagerDir(pathManagerClass, "getSystemPath");
        if (systemPath != null) {
            return new File(systemPath, "plugins");
        }
        return null;
    }

    private static List<File> selectPendingUpdateZips(File cacheDir, Set<File> pluginDirs) {
        List<File> pending = new ArrayList<>();
        if (cacheDir == null || !cacheDir.isDirectory()) {
            return pending;
        }
        File[] zips = cacheDir.listFiles((d, name) -> name.endsWith(ZIP_SUFFIX));
        if (zips == null) {
            return pending;
        }
        for (File zip : zips) {
            String name = zip.getName();
            String baseName = name.substring(0, name.length() - ZIP_SUFFIX.length());
            File installed = findInstalledDir(pluginDirs, baseName);
            // installed == null：插件未安装（卸载残留或全新待装），不在此扫描，避免拖慢启动；
            // zip 不比目录新：升级已应用，跳过。两种情况都无需解压。
            if (installed != null && zip.lastModified() > installed.lastModified()) {
                pending.add(zip);
            }
        }
        return pending;
    }

    private static File findInstalledDir(Set<File> pluginDirs, String baseName) {
        for (File dir : pluginDirs) {
            File candidate = new File(dir, baseName);
            if (candidate.isDirectory()) {
                return candidate;
            }
        }
        return null;
    }

    private static Class<?> loadClass(String className) {
        ClassLoader[] classLoaders = {
                Thread.currentThread().getContextClassLoader(),
                JbDirectoryScanner.class.getClassLoader(),
                ClassLoader.getSystemClassLoader()
        };
        for (ClassLoader classLoader : classLoaders) {
            Class<?> loaded = tryLoadClass(className, classLoader);
            if (loaded != null) {
                return loaded;
            }
        }
        return tryLoadClass(className, null);
    }

    private static Class<?> tryLoadClass(String className, ClassLoader classLoader) {
        try {
            if (classLoader == null) {
                return Class.forName(className);
            }
            return Class.forName(className, false, classLoader);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static File invokePathManagerDir(Class<?> pathManagerClass, String methodName) {
        Method method;
        try {
            method = pathManagerClass.getMethod(methodName);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
        try {
            return asFile(method.invoke(null));
        } catch (Exception e) {
            DebugInfo.error("[PRIVACY-SCAN] invokePathManagerDir failed (" + methodName + ")", e);
            return null;
        }
    }

    private static String invokePathManagerString(Class<?> pathManagerClass, String methodName) {
        if (pathManagerClass == null) {
            return null;
        }
        try {
            Method method = pathManagerClass.getMethod(methodName);
            Object value = method.invoke(null);
            return value == null ? null : String.valueOf(value);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static File asFile(Object value) {
        if (value instanceof File) {
            return (File) value;
        }
        if (value instanceof Path) {
            return ((Path) value).toFile();
        }
        if (value instanceof String && !isBlank((String) value)) {
            return new File((String) value);
        }
        return null;
    }

    private static void addDir(Set<File> dirs, File dir) {
        if (dir == null || !dir.isDirectory()) {
            return;
        }
        dirs.add(dir);
    }

    private static String stripSurroundingQuotes(String value) {
        if (value == null || value.length() < 2) {
            return value;
        }
        if ((value.startsWith("\"") && value.endsWith("\""))
                || (value.startsWith("'") && value.endsWith("'"))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static String getProperty(String name) {
        if (isBlank(name)) {
            return null;
        }
        try {
            return System.getProperty(name);
        } catch (SecurityException ignored) {
            return null;
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
