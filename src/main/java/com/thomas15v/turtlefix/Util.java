package com.thomas15v.turtlefix;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by thomas on 9/19/2014.
 */
public class Util {

    public static boolean TurtleCanBreakBlock(World world, int x, int y, int z){
        BlockEvent event =
                new BlockEvent.BreakEvent(x,y,z,world, Block.blocksList[world.getBlockId(x,y,z)],world.getBlockMetadata(x,y,z), FakePlayerFactory.get(
                        world, "[ComputerCraft]"));
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

    public static boolean BoreCanBreakBlock(World world, int x, int y, int z){
        BlockEvent event =
                new BlockEvent.BreakEvent(x,y,z,world, Block.blocksList[world.getBlockId(x,y,z)],world.getBlockMetadata(x,y,z), FakePlayerFactory.get(
                        world, "[FakeThaumcraftBore]"));
        return !event.isCanceled();
    }

    private static Printer printer = new Textifier();
    private static TraceMethodVisitor mp = new TraceMethodVisitor(printer);

    public static void printinstruction(InsnList list, String filename) {
        try {
            File file = new File(filename);
            file.createNewFile();
            PrintWriter out = new PrintWriter(filename);
            for (AbstractInsnNode node : list.toArray())
                out.println(insnToString(node));
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }
}
