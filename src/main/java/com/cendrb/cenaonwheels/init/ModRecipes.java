package com.cendrb.cenaonwheels.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

/**
 * Created by cendr_000 on 18.10.2016.
 */
public class ModRecipes {
    public static void init() {
        // klid storage
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageCap), " P ", " L ", "CBC", 'P', ModItems.klidProjector, 'L', Blocks.LAPIS_BLOCK, 'C', ModItems.klidManipulationCircuit, 'B', ModBlocks.machineBase);
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageBasicCore), "MTM", "MBM", "MTM", 'M', Items.GOLD_INGOT, 'T', ModItems.tempusShard, 'B', ModBlocks.machineBase);
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageAdvancedCore), "MTM", "MBM", "MTM", 'M', ModItems.klidMetal, 'T', ModItems.tempusShard, 'B', ModBlocks.machineBase);
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageUltimateCore), "MTM", "MBM", "MTM", 'M', ModItems.klidGem, 'T', ModItems.tempusShard, 'B', ModBlocks.machineBase);
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageCasing, 4), "ITI", "TIT", "ITI", 'I', Items.IRON_INGOT, 'T', ModItems.tempusShard);
        addShapelessRecipe(new ItemStack(ModBlocks.klidStorageGlass), new ItemStack(ModBlocks.klidStorageCasing), new ItemStack(Blocks.GLASS));

        // components
        addShapedRecipe(new ItemStack(ModItems.klidProjector), " T ", "TDT", " T ", 'T', ModItems.tempusShard, 'D', Items.DIAMOND);
        addShapedRecipe(new ItemStack(ModItems.klidManipulationCircuit), "LLL", "TGT", "LLL", 'L', new ItemStack(Items.DYE, 1, 4), 'G', Items.GOLD_INGOT, 'T', ModItems.tempusShard);
    }

    private static void addShapedRecipe(ItemStack itemStack, Object... data) {
        CraftingManager.getInstance().addRecipe(itemStack, data);
    }

    private static void addShapelessRecipe(ItemStack result, ItemStack... components) {
        CraftingManager.getInstance().addShapelessRecipe(result, components);
    }
}
