package com.novitechie.rules;

import com.janetfilter.core.commons.DebugInfo;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class VMOptionsReadRule {

    private static final String VMOPTIONS_SUFFIX = ".vmoptions";

    public static boolean shouldHide(Path path) {
        return isVMOptions(path) && StackTraceRule.check();
    }

    public static File redirectFile(File file) {
        if (isVMOptions(file) && StackTraceRule.check()) {
            return hookFile(file);
        }
        return file;
    }

    public static String redirectPath(String path) {
        if (isVMOptions(path) && StackTraceRule.check()) {
            return hookFile(new File(path)).getPath();
        }
        return path;
    }

    public static byte[] emptyBytes(Path path) {
        log(path);
        return new byte[0];
    }

    public static String emptyString(Path path) {
        log(path);
        return "";
    }

    public static List<String> emptyLines(Path path) {
        log(path);
        return Collections.emptyList();
    }

    public static InputStream emptyInputStream(Path path) {
        log(path);
        return new ByteArrayInputStream(new byte[0]);
    }

    public static BufferedReader emptyBufferedReader(Path path) {
        log(path);
        return new BufferedReader(new StringReader(""));
    }

    private static boolean isVMOptions(Path path) {
        return path != null && isVMOptions(path.toString());
    }

    private static boolean isVMOptions(File file) {
        return file != null && isVMOptions(file.getPath());
    }

    private static boolean isVMOptions(String path) {
        return path != null && path.toLowerCase(Locale.ROOT).endsWith(VMOPTIONS_SUFFIX);
    }

    private static File hookFile(File fallback) {
        Path path = VMOptionsRule.hook();
        return path == null ? fallback : path.toFile();
    }

    private static void log(Path path) {
        DebugInfo.output("======================Hide VMOptions read: " + path);
    }
}
