package com.thomas15v.turtlefix;

import com.thomas15v.turtlefix.patchengine.ClassUtil;
import com.thomas15v.turtlefix.patchengine.MethodUtil;
import com.thomas15v.turtlefix.patchengine.NavigationUtil;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Created by thomas on 9/23/2014.
 */
public class ThaumCraftBoreTransformer extends Transformer {

    @Override
    public boolean transform(String s, String s2, ClassNode classNode) {
        if (s.equalsIgnoreCase("thaumcraft.common.tiles.TileArcaneBore")){
            MethodNode methodNode = ClassUtil.getMethod(classNode, "dig", "()V" );


            InsnList iflist = new InsnList();
            LabelNode labelNode = new LabelNode();

            iflist.add(new VarInsnNode(ALOAD, 0));
            iflist.add(new FieldInsnNode(GETFIELD, "thaumcraft/common/tiles/TileArcaneBore", "field_70331_k", "Lnet/minecraft/world/World;" ));
            iflist.add(new VarInsnNode(ALOAD, 0));
            iflist.add(new FieldInsnNode(GETFIELD,"thaumcraft/common/tiles/TileArcaneBore", "digX", "I" ));
            iflist.add(new VarInsnNode(ALOAD, 0));
            iflist.add(new FieldInsnNode(GETFIELD,"thaumcraft/common/tiles/TileArcaneBore", "digY", "I" ));
            iflist.add(new VarInsnNode(ALOAD, 0));
            iflist.add(new FieldInsnNode(GETFIELD,"thaumcraft/common/tiles/TileArcaneBore", "digZ", "I" ));
            iflist.add(new MethodInsnNode(INVOKESTATIC, "com/thomas15v/turtlefix/Util", "BoreCanBreakBlock",
                                          "(Lnet/minecraft/world/World;III)Z"));
            iflist.add(new JumpInsnNode(IFEQ, labelNode));

            methodNode.instructions.insert(NavigationUtil.getNextLabel(MethodUtil.getLabelWithInsn(methodNode, new FieldInsnNode(GETFIELD, "thaumcraft/common/tiles/TileArcaneBore", "toDig", "Z"))), iflist);

            methodNode.instructions.insertBefore(MethodUtil.getLabelWithInsn(methodNode, new MethodInsnNode(INVOKESPECIAL, "thaumcraft/common/tiles/TileArcaneBore", "findNextBlockToDig", "()V")), labelNode);

            Util.printinstruction(methodNode.instructions, s + ".txt");

            return true;
        }
        return false;
    }
}
