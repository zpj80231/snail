package com.novitechie;

import com.janetfilter.core.plugin.MyTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class LicensingFacadeTransformer implements MyTransformer {
    @Override
    public String getHookClassName() {
        return "com/intellij/ui/LicensingFacade";
    }

    @Override
    public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
        ClassReader reader = new ClassReader(classBytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);
        for (MethodNode m : node.methods) {
            if ("getLicenseExpirationDate".equals(m.name)) {
                InsnList list = new InsnList();
                LabelNode L0 = new LabelNode();
                list.add(new MethodInsnNode(INVOKESTATIC, "com/novitechie/rules/StackTraceRule", "hook", "()Ljava/util/Date;", false));
                list.add(new InsnNode(DUP));
                list.add(new JumpInsnNode(IFNULL, L0));
                list.add(new InsnNode(ARETURN));
                list.add(L0);
                list.add(new InsnNode(POP));
                m.instructions.insert(list);
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
