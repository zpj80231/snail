package com.novitechie.scan;

import com.janetfilter.core.commons.DebugInfo;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;

public class JbDirectoryScanner {

    private static final String ZIP_SUFFIX = ".zip";

    private JbDirectoryScanner() {
    }

    public static List<File> getPluginDir() {
        Class<?> pathManagerClass = loadClass("com.intellij.openapi.application.PathManager");
        if (pathManagerClass == null) {
            DebugInfo.warn("[PRIVACY-SCAN] PathManager not found");
            return Collections.emptyList();
        }

        Set<File> pluginDirs = new LinkedHashSet<>();
        addDir(pluginDirs, invokePathManagerDir(pathManagerClass, "getPluginsDir"));
        addDir(pluginDirs, invokePathManagerDir(pathManagerClass, "getPluginsPath"));

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

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
