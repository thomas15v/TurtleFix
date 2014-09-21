package com.thomas15v.turtlefix;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import com.thomas15v.turtlefix.patchengine.ClassUtil;
import com.thomas15v.turtlefix.patchengine.MethodUtil;
import com.thomas15v.turtlefix.patchengine.NavigationUtil;

import java.util.logging.Logger;

/**
 * Created by thomas on 9/19/2014.
 */
public class TurtleBreakFixTransformer extends Transformer {

    private static Logger logger = Logger.getLogger("TurtleFix");

    @Override
    public boolean transform(String name, String transformedName, ClassNode classNode) {
        if (name.equalsIgnoreCase("dan200.computercraft.shared.turtle.upgrades.TurtleTool")){

            MethodNode methodNode = ClassUtil.getMethod(classNode, "dig", "(Ldan200/computercraft/api/turtle/ITurtleAccess;I)Ldan200/computercraft/api/turtle/TurtleCommandResult;");

            if (methodNode == null){
                logger.severe("[TurtleFix][Break] We couldn't found method dig(Ldan200/computercraft/api/turtle/ITurtleAccess;I)Ldan200/computercraft/api/turtle/TurtleCommandResult;");
                return false;
            }

            LabelNode node1 = NavigationUtil.getPreviousLabel(MethodUtil.getLabelWithInsn(methodNode, new MethodInsnNode(INVOKESPECIAL,
                                                                                         "dan200/computercraft/shared/turtle/upgrades/TurtleTool",
                                                                                         "getBlockDropped",
                                                                                         "(Lnet/minecraft/world/World;III)Ljava/util/ArrayList;")));
            if (node1 == null) {
                logger.severe("[TurtleFix][Break] Something went terrible when trying to find a injection point in the dig method, Your your using minecraftforge 964?");
                return false;
            }

            logger.info("[TurtleFix][Break] Injection Point found!! Here we goooooo ....");

            InsnList blockbreakcode = new InsnList();
            LabelNode label1 = new LabelNode();
            blockbreakcode.add( label1 );
            blockbreakcode.add(new VarInsnNode(ALOAD, 3));
            blockbreakcode.add(new VarInsnNode(ALOAD, 5));
            blockbreakcode.add(new FieldInsnNode(GETFIELD, "net/minecraft/util/ChunkCoordinates", "field_71574_a", "I"));
            blockbreakcode.add(new VarInsnNode(ALOAD, 5));
            blockbreakcode.add(new FieldInsnNode(GETFIELD, "net/minecraft/util/ChunkCoordinates", "field_71572_b", "I"));
            blockbreakcode.add(new VarInsnNode(ALOAD, 5));
            blockbreakcode.add(new FieldInsnNode(GETFIELD, "net/minecraft/util/ChunkCoordinates", "field_71573_c", "I"));
            blockbreakcode.add(new MethodInsnNode(INVOKESTATIC, "com/thomas15v/turtlefix/Util", "TurtleCanBreakBlock",
                                                  "(Lnet/minecraft/world/World;III)Z"));
            LabelNode label2 = new LabelNode();
            blockbreakcode.add(new JumpInsnNode(IFEQ, label2));

            methodNode.instructions.insert(node1, blockbreakcode);

            LabelNode last = NavigationUtil.getNextLabel(MethodUtil.getLabelWithInsn(methodNode, new MethodInsnNode(INVOKESTATIC, "dan200/computercraft/api/turtle/TurtleCommandResult", "success", "()Ldan200/computercraft/api/turtle/TurtleCommandResult;")));

            methodNode.instructions.insertBefore(last, label2);


            logger.info("[TurtleFix][Break] Patch done!");
            return true;
        }else
        {
            return false;
        }
    }
}
