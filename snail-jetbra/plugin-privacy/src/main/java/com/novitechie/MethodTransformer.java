package com.novitechie;

import com.janetfilter.core.plugin.MyTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author YeloChick
 */
public class MethodTransformer implements MyTransformer {

    @Override
    public String getHookClassName() {
        return "java/lang/reflect/Method";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode m : node.methods) {
            if ("invoke".equals(m.name) && m.desc.equals("(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;")) {
                InsnList list = new InsnList();
                LabelNode L0 = new LabelNode();
                list.add(new LdcInsnNode("java.lang.ClassLoader"));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "java/lang/reflect/Method", "clazz", "Ljava/lang/Class;"));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                list.add(new JumpInsnNode(IFEQ, L0));
                list.add(new LdcInsnNode("findBootstrapClass"));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "java/lang/reflect/Method", "name", "Ljava/lang/String;"));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                list.add(new JumpInsnNode(IFEQ, L0));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new InsnNode(ICONST_0));
                list.add(new InsnNode(AALOAD));
                list.add(new MethodInsnNode(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;", false));
                list.add(new VarInsnNode(ASTORE, 3));
                // list.add(new LdcInsnNode("com.janetfilter"));
                list.add(new VarInsnNode(ALOAD, 3));
                // list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/BootstrapClassRule", "check", "(Ljava/lang/String;)Z", false));
                list.add(new JumpInsnNode(IFEQ, L0));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/StackTraceRule", "check", "()Z", false));
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
