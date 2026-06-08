package com.novitechie;

import com.janetfilter.core.plugin.MyTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class FileInputStreamTransformer implements MyTransformer {

    private static final String RULE = "com/novitechie/rules/VMOptionsReadRule";

    @Override
    public String getHookClassName() {
        return "java/io/FileInputStream";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode method : node.methods) {
            if ("<init>".equals(method.name) && "(Ljava/io/File;)V".equals(method.desc)) {
                insertRedirect(method, "redirectFile", "(Ljava/io/File;)Ljava/io/File;");
            } else if ("<init>".equals(method.name) && "(Ljava/lang/String;)V".equals(method.desc)) {
                insertRedirect(method, "redirectPath", "(Ljava/lang/String;)Ljava/lang/String;");
            }
        }
        ClassWriter writer = new SafeClassWriter(reader, null, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }

    private static void insertRedirect(MethodNode method, String hookName, String hookDesc) {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new MethodInsnNode(INVOKESTATIC, RULE, hookName, hookDesc, false));
        list.add(new VarInsnNode(ASTORE, 1));
        method.instructions.insert(list);
    }
}
