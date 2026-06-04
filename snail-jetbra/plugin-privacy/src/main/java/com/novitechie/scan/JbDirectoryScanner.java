package com.novitechie.scan;

import com.janetfilter.core.commons.DebugInfo;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;

public class JbDirectoryScanner {

    private JbDirectoryScanner() {
    }

    public static List<File> getPluginDir() {
        Class<?> pathManagerClass = loadClass("com.intellij.openapi.application.PathManager");
        if (pathManagerClass == null) {
            DebugInfo.warn("[PRIVACY-SCAN] PathManager not found");
            return Collections.emptyList();
        }

        Set<File> pluginRoots = new LinkedHashSet<>();
        addDir(pluginRoots, invokePathManagerDir(pathManagerClass, "getPluginsDir"));
        addDir(pluginRoots, invokePathManagerDir(pathManagerClass, "getPluginsPath"));

        DebugInfo.output("[PRIVACY-SCAN] PathManager plugin roots: " + pluginRoots);
        return new ArrayList<>(pluginRoots);
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
        try {
            Method method = pathManagerClass.getMethod(methodName);
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
