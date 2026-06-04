package com.novitechie;

import com.janetfilter.core.plugin.MyTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class VMOptionsTransformer implements MyTransformer {
    @Override
    public String getHookClassName() {
        return "com/intellij/diagnostic/VMOptions";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode m : node.methods) {
            if ("getUserOptionsFile".equals(m.name)) {
                InsnList list = new InsnList();
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/StackTraceRule", "check", "()Z", false));
                LabelNode labelNode = new LabelNode();
                list.add(new JumpInsnNode(IFEQ, labelNode));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/VMOptionsRule", "hook", "()Ljava/nio/file/Path;", false));
                list.add(new InsnNode(ARETURN));
                list.add(labelNode);
                m.instructions.insert(list);
            } else if ("readOption".equals(m.name) && "(Ljava/lang/String;Z)Ljava/lang/String;".equals(m.desc)) {
                InsnList list = new InsnList();
                LabelNode continueLabel = new LabelNode();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new LdcInsnNode("javaagent"));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false));
                list.add(new JumpInsnNode(IFEQ, continueLabel));
                list.add(new InsnNode(ACONST_NULL));
                list.add(new InsnNode(ARETURN));
                list.add(continueLabel);
                m.instructions.insert(list);
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
