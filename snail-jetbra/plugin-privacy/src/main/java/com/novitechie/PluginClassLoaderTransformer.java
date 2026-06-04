package com.novitechie;

import com.janetfilter.core.plugin.MyTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class PluginClassLoaderTransformer implements MyTransformer {
    @Override
    public String getHookClassName() {
        return "com/intellij/ide/plugins/cl/PluginClassLoader";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode m : node.methods) {
            if ("loadClass".equals(m.name)) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/LoadClassRule", "check", "(Ljava/lang/String;)V", false));
                m.instructions.insert(list);
            }
        }
        ClassWriter writer = new SafeClassWriter(null, null, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}