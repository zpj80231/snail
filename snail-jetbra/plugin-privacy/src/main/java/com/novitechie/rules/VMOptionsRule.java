package com.novitechie.rules;

import com.janetfilter.core.commons.DebugInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author YeloChick
 */
public class VMOptionsRule {

    public static Path hook() {
        DebugInfo.output("======================Hide VMOptions");
        String tmpdir = System.getProperty("java.io.tmpdir");
        File file = new File(tmpdir, "fake.vmoptions");
        if (file.exists()) {
            return file.toPath();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("# This file is created by plugin-privacy".getBytes());
            return file.toPath();
        } catch (IOException e) {
            DebugInfo.output("create temp file error", e);
            return null;
        }
    }
}
