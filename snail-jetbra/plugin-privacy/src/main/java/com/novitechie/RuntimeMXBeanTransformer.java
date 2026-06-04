package com.novitechie;

import com.janetfilter.core.plugin.MyTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author YeloChick
 */
public class RuntimeMXBeanTransformer implements MyTransformer {

    @Override
    public String getHookClassName() {
        return "sun/management/RuntimeImpl";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode m : node.methods) {
            if ("getInputArguments".equals(m.name)) {
                InsnList list = new InsnList();
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/StackTraceRule", "check", "()Z", false));
                LabelNode labelNode = new LabelNode();
                list.add(new JumpInsnNode(IFEQ, labelNode));
                list.add(new LdcInsnNode("sorry, you can not read anything. haha!"));
                list.add(new MethodInsnNode(INVOKESTATIC, "java/util/Collections", "singletonList", "(Ljava/lang/Object;)Ljava/util/List;", false));
                list.add(new InsnNode(ARETURN));
                list.add(labelNode);
                m.instructions.insert(list);
            }
        }
        ClassWriter writer = new SafeClassWriter(null, null, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
