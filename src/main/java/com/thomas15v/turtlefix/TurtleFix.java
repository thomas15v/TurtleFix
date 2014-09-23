package com.thomas15v.turtlefix;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * Created by thomas on 9/19/2014.
 */
public class TurtleFix implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"com.thomas15v.turtlefix.TurtleBreakFixTransformer",
                            "com.thomas15v.turtlefix.ThaumCraftBoreTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }
}
