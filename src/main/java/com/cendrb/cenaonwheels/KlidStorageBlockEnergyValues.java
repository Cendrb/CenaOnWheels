package com.cendrb.cenaonwheels;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class VisStorageBlockEnergyValues {

    private static HashMap<Block, Integer> blockValues = new HashMap<>();

    static {
        blockValues.put(Blocks.IRON_BLOCK, 50);
        blockValues.put(Blocks.GOLD_BLOCK, 200);
        blockValues.put(Blocks.DIAMOND_ORE, 500);
    }

    public static Integer getEnergyValue(Block block)
    {
        return blockValues.get(block);
    }
}
