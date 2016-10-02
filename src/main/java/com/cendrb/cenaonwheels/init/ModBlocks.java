package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.block.BlockBase;
import com.cendrb.cenaonwheels.block.BlockOreTempus;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ModBlocks {

    public static BlockOreTempus oreTempus;

    public static void init() {
        oreTempus = registerWithDefaultItemblock(new BlockOreTempus());
    }

    private static <T extends BlockBase> T register(T block, ItemBlock itemBlock) {

        GameRegistry.register(block);
        GameRegistry.register(itemBlock);
        block.registerItemModel(itemBlock);
        return block;
    }

    private static <T extends BlockBase> T registerWithDefaultItemblock(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return register(block, itemBlock);
    }
}
