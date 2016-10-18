package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.block.*;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ModBlocks {

    public static BlockOreTempus oreTempus;
    public static BlockKlidStorageCap klidStorageCap;
    public static BlockKlidStorageCasing klidStorageCasing;
    public static BlockKlidStorageGlass klidStorageGlass;
    public static BlockKlidStorageCore klidStorageCore;
    public static BlockCowKlidGenerator cowKlidGenerator;

    public static void init() {
        oreTempus = registerWithDefaultItemblock(new BlockOreTempus());
        klidStorageCap = registerWithDefaultItemblock(new BlockKlidStorageCap());
        klidStorageCasing = registerWithDefaultItemblock(new BlockKlidStorageCasing());
        klidStorageGlass = registerWithDefaultItemblock(new BlockKlidStorageGlass());
        klidStorageCore = registerWithDefaultItemblock(new BlockKlidStorageCore());
        cowKlidGenerator = registerWithDefaultItemblock(new BlockCowKlidGenerator());
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
