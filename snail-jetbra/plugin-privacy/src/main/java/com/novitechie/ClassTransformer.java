package com.novitechie;

import com.janetfilter.core.plugin.MyTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author YeloChick
 */
public class ClassTransformer implements MyTransformer {

    @Override
    public String getHookClassName() {
        return "java/lang/Class";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode m : node.methods) {
            if ("getResource".equals(m.name) && m.desc.equals("(Ljava/lang/String;)Ljava/net/URL;")) {
                InsnList list = new InsnList();
                LabelNode L0 = new LabelNode();
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/ResourceRule", "check", "(Ljava/lang/String;)Z", false));
                list.add(new JumpInsnNode(IFEQ, L0));
                list.add(new InsnNode(ACONST_NULL));
                list.add(new InsnNode(ARETURN));
                list.add(L0);
                m.instructions.insert(list);
            } else if ("getResourceAsStream".equals(m.name) && m.desc.equals("(Ljava/lang/String;)Ljava/io/InputStream;")) {
                InsnList list = new InsnList();
                LabelNode L0 = new LabelNode();
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/ResourceRule", "check", "(Ljava/lang/String;)Z", false));
                list.add(new JumpInsnNode(IFEQ, L0));
                list.add(new InsnNode(ACONST_NULL));
                list.add(new InsnNode(ARETURN));
                list.add(L0);
                m.instructions.insert(list);
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
