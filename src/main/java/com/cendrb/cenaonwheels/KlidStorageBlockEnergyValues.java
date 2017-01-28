package com.cendrb.cenaonwheels;

import com.cendrb.cenaonwheels.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class KlidStorageBlockEnergyValues {

    private static HashMap<Block, Integer> blockValues = new HashMap<>();

    static {
        blockValues.put(Blocks.IRON_BLOCK, 50);
        blockValues.put(Blocks.GOLD_BLOCK, 200);
        blockValues.put(ModBlocks.klidMetalBlock, 400);
        blockValues.put(Blocks.DIAMOND_BLOCK, 500);
    }

    public static Integer getEnergyValue(Block block) {
        return blockValues.get(block);
    }
}
