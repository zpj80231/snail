package com.novitechie;

import com.janetfilter.core.plugin.MyTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author YeloChick
 */
public class PluginManagerCoreTransformer implements MyTransformer {

    @Override
    public String getHookClassName() {
        return "com/intellij/ide/plugins/PluginManagerCore";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode m : node.methods) {
            if ("getPlugins".equals(m.name)) {
                InsnList list = new InsnList();
                LabelNode L0 = new LabelNode();
                LabelNode L1 = new LabelNode();
                LabelNode L2 = new LabelNode();
                LabelNode L3 = new LabelNode();
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/StackTraceRule", "check", "()Z", false));
                list.add(new JumpInsnNode(IFEQ, L0));
                list.add(new FieldInsnNode(GETSTATIC, "com/intellij/ide/plugins/PluginManagerCore", "nullablePluginSet", "Lcom/intellij/ide/plugins/PluginSet;"));
                list.add(new FieldInsnNode(GETFIELD, "com/intellij/ide/plugins/PluginSet", "allPlugins", "Ljava/util/Set;"));
                list.add(new VarInsnNode(ASTORE, 0));
                list.add(new TypeInsnNode(NEW, "java/util/HashSet"));
                list.add(new InsnNode(DUP));
                list.add(new MethodInsnNode(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V", false));
                list.add(new VarInsnNode(ASTORE, 1));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Set", "iterator", "()Ljava/util/Iterator;", true));
                list.add(new VarInsnNode(ASTORE, 2));
                list.add(L2);
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true));
                list.add(new JumpInsnNode(IFEQ, L1));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true));
                list.add(new TypeInsnNode(CHECKCAST, "com/intellij/ide/plugins/IdeaPluginDescriptor"));
                list.add(new VarInsnNode(ASTORE, 3));
                list.add(new VarInsnNode(ALOAD, 3));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "com/intellij/ide/plugins/IdeaPluginDescriptor", "getPluginId", "()Lcom/intellij/openapi/extensions/PluginId;", true));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/intellij/openapi/extensions/PluginId", "getIdString", "()Ljava/lang/String;", false));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/IdeaPluginRule", "check", "(Ljava/lang/String;)Z", false));
                list.add(new JumpInsnNode(IFNE, L3));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new VarInsnNode(ALOAD, 3));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Set", "add", "(Ljava/lang/Object;)Z", true));
                list.add(new InsnNode(POP));
                list.add(L3);
                list.add(new JumpInsnNode(GOTO, L2));
                list.add(L1);
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new InsnNode(ICONST_0));
                list.add(new TypeInsnNode(ANEWARRAY, "com/intellij/ide/plugins/IdeaPluginDescriptor"));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Set", "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;", true));
                list.add(new TypeInsnNode(CHECKCAST, "[Lcom/intellij/ide/plugins/IdeaPluginDescriptor;"));
                list.add(new InsnNode(ARETURN));
                list.add(L0);
                m.instructions.insert(list);
            } else if ("isPluginInstalled".equals(m.name)) {
                InsnList list = new InsnList();
                LabelNode L0 = new LabelNode();
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/StackTraceRule", "check", "()Z", false));
                list.add(new JumpInsnNode(IFEQ, L0));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/intellij/openapi/extensions/PluginId", "getIdString", "()Ljava/lang/String;", false));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/IdeaPluginRule", "check", "(Ljava/lang/String;)Z", false));
                list.add(new JumpInsnNode(IFEQ, L0));
                list.add(new InsnNode(ICONST_0));
                list.add(new InsnNode(IRETURN));
                list.add(L0);
                m.instructions.insert(list);
            } else if ("isDisabled".equals(m.name)) {
                InsnList list = new InsnList();
                LabelNode L0 = new LabelNode();
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/StackTraceRule", "check", "()Z", false));
                list.add(new JumpInsnNode(IFEQ, L0));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/intellij/openapi/extensions/PluginId", "getIdString", "()Ljava/lang/String;", false));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/IdeaPluginRule", "check", "(Ljava/lang/String;)Z", false));
                list.add(new JumpInsnNode(IFEQ, L0));
                list.add(new InsnNode(ICONST_1));
                list.add(new InsnNode(IRETURN));
                list.add(L0);
                m.instructions.insert(list);
            }
        }
        ClassWriter writer = new SafeClassWriter(null, null, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
