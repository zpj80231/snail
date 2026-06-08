package com.novitechie;

import com.janetfilter.core.plugin.MyTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class FilesTransformer implements MyTransformer {

    private static final String RULE = "com/novitechie/rules/VMOptionsReadRule";
    private static final String PATH_DESC = "(Ljava/nio/file/Path;)Z";

    @Override
    public String getHookClassName() {
        return "java/nio/file/Files";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode method : node.methods) {
            if ("readAllBytes".equals(method.name) && "(Ljava/nio/file/Path;)[B".equals(method.desc)) {
                insertPathGuard(method, "emptyBytes", "(Ljava/nio/file/Path;)[B");
            } else if ("readString".equals(method.name)
                    && ("(Ljava/nio/file/Path;)Ljava/lang/String;".equals(method.desc)
                    || "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;)Ljava/lang/String;".equals(method.desc))) {
                insertPathGuard(method, "emptyString", "(Ljava/nio/file/Path;)Ljava/lang/String;");
            } else if ("readAllLines".equals(method.name)
                    && ("(Ljava/nio/file/Path;)Ljava/util/List;".equals(method.desc)
                    || "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;)Ljava/util/List;".equals(method.desc))) {
                insertPathGuard(method, "emptyLines", "(Ljava/nio/file/Path;)Ljava/util/List;");
            } else if ("newInputStream".equals(method.name)
                    && "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/io/InputStream;".equals(method.desc)) {
                insertPathGuard(method, "emptyInputStream", "(Ljava/nio/file/Path;)Ljava/io/InputStream;");
            } else if ("newBufferedReader".equals(method.name)
                    && ("(Ljava/nio/file/Path;)Ljava/io/BufferedReader;".equals(method.desc)
                    || "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;)Ljava/io/BufferedReader;".equals(method.desc))) {
                insertPathGuard(method, "emptyBufferedReader", "(Ljava/nio/file/Path;)Ljava/io/BufferedReader;");
            }
        }
        ClassWriter writer = new SafeClassWriter(reader, null, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }

    private static void insertPathGuard(MethodNode method, String hookName, String hookDesc) {
        InsnList list = new InsnList();
        LabelNode continueLabel = new LabelNode();
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new MethodInsnNode(INVOKESTATIC, RULE, "shouldHide", PATH_DESC, false));
        list.add(new JumpInsnNode(IFEQ, continueLabel));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new MethodInsnNode(INVOKESTATIC, RULE, hookName, hookDesc, false));
        list.add(new InsnNode(ARETURN));
        list.add(continueLabel);
        method.instructions.insert(list);
    }
}
