package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.block.*;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ModBlocks {

    // general
    public static BlockOreTempus oreTempus;
    public static BlockMachineBase machineBase;

    // klid storage
    public static BlockKlidStorageCap klidStorageCap;
    public static BlockKlidStorageCasing klidStorageCasing;
    public static BlockKlidStorageGlass klidStorageGlass;
    public static BlockKlidStorageCore klidStorageBasicCore;
    public static BlockKlidStorageCore klidStorageAdvancedCore;
    public static BlockKlidStorageCore klidStorageUltimateCore;

    // klid infusion
    public static BlockKlidInfusionPlate klidInfusionBasicPlate;
    public static BlockKlidInfusionPlate klidInfusionAdvancedPlate;
    public static BlockKlidInfusionPlate klidInfusionUltimatePlate;

    // klid generator
    public static BlockCowKlidGenerator cowKlidGenerator;

    public static void init() {
        oreTempus = registerWithDefaultItemblock(new BlockOreTempus());
        machineBase = registerWithDefaultItemblock(new BlockMachineBase());
        klidStorageCap = registerWithDefaultItemblock(new BlockKlidStorageCap());
        klidStorageCasing = registerWithDefaultItemblock(new BlockKlidStorageCasing());
        klidStorageGlass = registerWithDefaultItemblock(new BlockKlidStorageGlass());
        klidStorageBasicCore = registerWithDefaultItemblock(new BlockKlidStorageCore("Basic", 20));
        klidStorageAdvancedCore = registerWithDefaultItemblock(new BlockKlidStorageCore("Advanced", 100));
        klidStorageUltimateCore = registerWithDefaultItemblock(new BlockKlidStorageCore("Ultimate", 500));
        cowKlidGenerator = registerWithDefaultItemblock(new BlockCowKlidGenerator());
        klidInfusionBasicPlate = registerWithDefaultItemblock(new BlockKlidInfusionPlate("Basic", 0.3f));
        klidInfusionAdvancedPlate = registerWithDefaultItemblock(new BlockKlidInfusionPlate("Advanced", 0.7f));
        klidInfusionUltimatePlate = registerWithDefaultItemblock(new BlockKlidInfusionPlate("Ultimate", 1.0f));
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
