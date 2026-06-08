package com.novitechie.scan;

import com.janetfilter.core.models.FilterRule;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class CanaryScanner {

    private static final String JAR_SUFFIX = ".jar";
    private static final String ZIP_SUFFIX = ".zip";
    private static final String CLASS_SUFFIX = ".class";
    private static final String PLUGIN_XML = "META-INF/plugin.xml";
    private static final String MARKER_RESOURCE = "6c81ec87e55d331c267262e892427a3d93d76683.txt";
    private static final String LIB_DIR = "lib";
    private static final String ID_TAG = "id";
    private static final Pattern COMMENT_PATTERN = Pattern.compile("<!--.*?-->", Pattern.DOTALL);
    public static final Pattern TAG_PATTERN = Pattern.compile(
            "<" + Pattern.quote(ID_TAG) + "(?:\\s[^>]*)?>(.*?)</" + Pattern.quote(ID_TAG) + ">",
            Pattern.DOTALL);

    private CanaryScanner() {
    }

    public static class ScanResult {
        private final Set<String> classNames = new LinkedHashSet<>();
        private boolean markerResourceExists;

        public List<String> getClassNames() {
            return new ArrayList<>(classNames);
        }

        public boolean isMarkerResourceExists() {
            return markerResourceExists;
        }

        private void addClassName(String className) {
            classNames.add(className);
        }

        private void markResourceExists() {
            markerResourceExists = true;
        }
    }

    public static ScanResult scan(List<File> scanTargets, List<FilterRule> scanPluginRules,
                                  List<FilterRule> scanPackageRules, List<FilterRule> excludePackageRules) {
        ScanResult result = new ScanResult();
        if (scanTargets == null || scanTargets.isEmpty()) {
            return result;
        }
        for (File target : scanTargets) {
            if (target != null) {
                scanTarget(target, scanPluginRules, scanPackageRules, excludePackageRules, result);
            }
        }
        return result;
    }

    private static void scanTarget(File target, List<FilterRule> scanPluginRules,
                                   List<FilterRule> scanPackageRules, List<FilterRule> excludePackageRules,
                                   ScanResult result) {
        if (isJar(target)) {
            processJarIfMatches(target, scanPluginRules, scanPackageRules, excludePackageRules, result);
            return;
        }

        if (isZip(target)) {
            processZipIfMatches(target, scanPluginRules, scanPackageRules, excludePackageRules, result);
            return;
        }

        if (target == null || !target.isDirectory()) {
            return;
        }

        File xmlFile = new File(target, PLUGIN_XML);
        if (xmlFile.isFile()) {
            String pluginId = parsePluginIdFromXml(xmlFile);
            if (matchRules(pluginId, scanPluginRules)) {
                scanPluginDirectoryContent(target, scanPackageRules, excludePackageRules, result);
            }
            return;
        }

        File libDir = new File(target, LIB_DIR);
        if (libDir.isDirectory()) {
            File[] jars = listJars(libDir);
            if (jars != null && jars.length > 0) {
                String pluginId = resolvePluginIdFromJars(jars);
                if (matchRules(pluginId, scanPluginRules)) {
                    scanJars(jars, scanPackageRules, excludePackageRules, result);
                }
                return;
            }
        }

        File[] subFiles = target.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File subFile : subFiles) {
            scanTarget(subFile, scanPluginRules, scanPackageRules, excludePackageRules, result);
        }
    }

    private static void scanPluginDirectoryContent(File pluginDir, List<FilterRule> scanPackageRules,
                                                   List<FilterRule> excludePackageRules, ScanResult result) {
        File libDir = new File(pluginDir, LIB_DIR);
        if (!libDir.isDirectory()) {
            return;
        }
        scanJars(listJars(libDir), scanPackageRules, excludePackageRules, result);
    }

    private static File[] listJars(File dir) {
        return dir.listFiles((d, name) -> name.endsWith(JAR_SUFFIX));
    }

    private static void scanJars(File[] jars, List<FilterRule> scanPackageRules,
                                 List<FilterRule> excludePackageRules, ScanResult result) {
        if (jars == null) {
            return;
        }
        for (File jar : jars) {
            scanJarClasses(jar, scanPackageRules, excludePackageRules, result);
        }
    }

    private static void processJarIfMatches(File jarFile, List<FilterRule> scanPluginRules,
                                            List<FilterRule> scanPackageRules, List<FilterRule> excludePackageRules,
                                            ScanResult result) {
        try (JarFile jar = new JarFile(jarFile)) {
            String pluginId = parsePluginId(jar);
            if (matchRules(pluginId, scanPluginRules)) {
                extractClasses(jar, scanPackageRules, excludePackageRules, result);
            }
        } catch (IOException ignored) {
            // 静默失效，确保健壮性
        }
    }

    private static void scanJarClasses(File jarFile, List<FilterRule> scanPackageRules,
                                       List<FilterRule> excludePackageRules, ScanResult result) {
        try (JarFile jar = new JarFile(jarFile)) {
            extractClasses(jar, scanPackageRules, excludePackageRules, result);
        } catch (IOException ignored) {
            // 忽略损坏的包
        }
    }

    private static String resolvePluginIdFromJars(File[] jars) {
        for (File jarFile : jars) {
            try (JarFile jar = new JarFile(jarFile)) {
                String id = parsePluginId(jar);
                if (id != null) {
                    return id;
                }
            } catch (IOException ignored) {
                // 轮询下一个包
            }
        }
        return null;
    }

    private static String parsePluginIdFromXml(File xmlFile) {
        try (FileInputStream fis = new FileInputStream(xmlFile)) {
            return extractXmlTag(readUtf8(fis));
        } catch (IOException e) {
            return null;
        }
    }

    private static String parsePluginId(JarFile jar) throws IOException {
        JarEntry entry = jar.getJarEntry(PLUGIN_XML);
        if (entry == null) {
            return null;
        }
        try (InputStream is = jar.getInputStream(entry)) {
            return extractXmlTag(readUtf8(is));
        }
    }

    private static String readUtf8(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        byte[] chunk = new byte[1024];
        int read;
        while ((read = input.read(chunk)) != -1) {
            output.write(chunk, 0, read);
        }
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }

    private static String extractXmlTag(String content) {
        String withoutComments = COMMENT_PATTERN.matcher(content).replaceAll("");
        Matcher matcher = TAG_PATTERN.matcher(withoutComments);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    private static void extractClasses(JarFile jar, List<FilterRule> scanPackageRules,
                                       List<FilterRule> excludePackageRules, ScanResult result) {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (MARKER_RESOURCE.equals(name)) {
                result.markResourceExists();
            }
            if (!name.endsWith(CLASS_SUFFIX)) {
                continue;
            }
            String className = name.substring(0, name.length() - CLASS_SUFFIX.length()).replace('/', '.');
            if (matchRules(className, scanPackageRules) && !matchRules(className, excludePackageRules)) {
                result.addClassName(className);
            }
        }
    }

    private static boolean isJar(File file) {
        return file != null && file.isFile() && file.getName().endsWith(JAR_SUFFIX);
    }

    private static boolean isZip(File file) {
        return file != null && file.isFile() && file.getName().endsWith(ZIP_SUFFIX);
    }

    /**
     * 处理 Marketplace 下载缓存中的插件 zip：典型结构为 {@code <top>/lib/*.jar}。
     * <p>
     * 该缓存目录可能有数十个、体量上百 MB 的插件 zip，每次启动都会扫描。为避免把不匹配
     * 规则的插件全部读入内存，这里用 {@link ZipFile} 随机访问外层 zip，并分两遍：
     * 先只读内层 jar 的 plugin.xml 判定插件 id；命中规则后再流式提取 class。
     */
    private static void processZipIfMatches(File zipFile, List<FilterRule> scanPluginRules,
                                            List<FilterRule> scanPackageRules, List<FilterRule> excludePackageRules,
                                            ScanResult result) {
        try (ZipFile zip = new ZipFile(zipFile)) {
            String pluginId = resolvePluginIdFromZip(zip);
            if (!matchRules(pluginId, scanPluginRules)) {
                return;
            }
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory() || !entry.getName().endsWith(JAR_SUFFIX)) {
                    continue;
                }
                try (InputStream is = zip.getInputStream(entry)) {
                    extractClassesFromJarStream(is, scanPackageRules, excludePackageRules, result);
                }
            }
        } catch (IOException ignored) {
            // 损坏或非标准 zip，忽略
        }
    }

    private static String resolvePluginIdFromZip(ZipFile zip) {
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory() || !entry.getName().endsWith(JAR_SUFFIX)) {
                continue;
            }
            try (InputStream is = zip.getInputStream(entry)) {
                String id = parsePluginIdFromJarStream(is);
                if (id != null) {
                    return id;
                }
            } catch (IOException ignored) {
                // 跳过损坏的内层 jar
            }
        }
        return null;
    }

    private static String parsePluginIdFromJarStream(InputStream jarStream) throws IOException {
        // ZipInputStream 关闭时只会结束自身的 Inflater，外层 jarStream 由调用方 try-with 关闭
        ZipInputStream zis = new ZipInputStream(jarStream);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (PLUGIN_XML.equals(entry.getName())) {
                return extractXmlTag(readUtf8(zis));
            }
        }
        return null;
    }

    private static void extractClassesFromJarStream(InputStream jarStream, List<FilterRule> scanPackageRules,
                                                    List<FilterRule> excludePackageRules, ScanResult result)
            throws IOException {
        ZipInputStream zis = new ZipInputStream(jarStream);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            String name = entry.getName();
            if (MARKER_RESOURCE.equals(name)) {
                result.markResourceExists();
            }
            if (!name.endsWith(CLASS_SUFFIX)) {
                continue;
            }
            String className = name.substring(0, name.length() - CLASS_SUFFIX.length()).replace('/', '.');
            if (matchRules(className, scanPackageRules) && !matchRules(className, excludePackageRules)) {
                result.addClassName(className);
            }
        }
    }

    private static boolean matchRules(String value, List<FilterRule> rules) {
        if (value == null || rules == null || rules.isEmpty()) {
            return false;
        }
        for (FilterRule rule : rules) {
            if (rule != null && rule.test(value)) {
                return true;
            }
        }
        return false;
    }
}
