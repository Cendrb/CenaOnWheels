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
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageCap), "TTT", "TLT", "CBC", 'L', Blocks.LAPIS_BLOCK, 'C', ModItems.klidManipulationCircuit, 'B', ModBlocks.machineBase, 'T', ModItems.tempusShard);
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageBasicCore), "MTM", "MBM", "MTM", 'M', Items.GOLD_INGOT, 'T', ModItems.tempusShard, 'B', ModBlocks.machineBase);
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageAdvancedCore), "MTM", "MBM", "MTM", 'M', ModItems.klidMetal, 'T', ModItems.tempusShard, 'B', ModBlocks.machineBase);
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageUltimateCore), "MTM", "MBM", "MTM", 'M', ModItems.klidGem, 'T', ModItems.tempusShard, 'B', ModBlocks.machineBase);
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageCasing, 4), "ITI", "TIT", "ITI", 'I', Items.IRON_INGOT, 'T', ModItems.tempusShard);
        addShapelessRecipe(new ItemStack(ModBlocks.klidStorageGlass), new ItemStack(ModBlocks.klidStorageCasing), new ItemStack(Blocks.GLASS));
        addShapedRecipe(new ItemStack(ModBlocks.klidStorageExtractor), " P ", " T ", "CBC", 'P', ModItems.klidProjector, 'C', ModItems.klidManipulationCircuit, 'B', ModBlocks.machineBase, 'T', ModItems.tempusShard);

        // klid generation
        addShapedRecipe(new ItemStack(ModBlocks.cowKlidGenerator), "CPC", "TBT", "TTT", 'T', ModItems.tempusShard, 'P', ModItems.klidProjector, 'C', ModItems.klidManipulationCircuit, 'B', ModBlocks.machineBase);

        // klid infusion
        addShapedRecipe(new ItemStack(ModBlocks.klidInfusionBasicPlate), " C ", "GDG", "GBG", 'C', ModItems.klidManipulationCircuit, 'G', Items.GOLD_INGOT, 'D', Items.DIAMOND, 'B', ModBlocks.machineBase);
        addShapedRecipe(new ItemStack(ModBlocks.klidInfusionAdvancedPlate), " C ", "MGM", "MBM", 'C', ModItems.klidManipulationCircuit, 'M', ModItems.klidMetal, 'G', ModItems.klidGem, 'B', ModBlocks.machineBase);
        addShapedRecipe(new ItemStack(ModBlocks.klidInfusionUltimatePlate), " C ", "GAG", "GBG", 'C', ModItems.klidManipulationCircuit, 'G', ModItems.klidGem, 'A', ModItems.aschGem, 'B', ModBlocks.machineBase);

        // components
        addShapedRecipe(new ItemStack(ModItems.klidProjector), " T ", "TDT", " T ", 'T', ModItems.tempusShard, 'D', Items.DIAMOND);
        addShapedRecipe(new ItemStack(ModItems.klidManipulationCircuit), "LLL", "TGT", "LLL", 'L', new ItemStack(Items.DYE, 1, 4), 'G', Items.GOLD_INGOT, 'T', ModItems.tempusShard);
        addShapedRecipe(new ItemStack(ModBlocks.machineBase), "ITI", "TGT", "ITI", 'T', ModItems.tempusShard, 'I', Items.IRON_INGOT, 'G', Items.GOLD_INGOT);
        addShapedRecipe(new ItemStack(ModBlocks.tempusBlock), "TTT", "TTT", "TTT", 'T', ModItems.tempusShard);

        // wrench
        addShapedRecipe(new ItemStack(ModItems.wrench), "I I", " T ", " I ", 'T', ModItems.tempusShard, 'I', Items.IRON_INGOT);

        // blocks
        addShapedRecipe(new ItemStack(ModBlocks.klidMetalBlock), "III", "III", "III", 'I', ModItems.klidMetal);

        // trees
        addShapelessRecipe(new ItemStack(ModBlocks.klidPlanks, 4), new ItemStack(ModBlocks.klidLog));

        // heim
        addShapedRecipe(new ItemStack(ModBlocks.heim), "TGT", "GTG", "TGT", 'T', ModItems.timelessGunpowder, 'G', Items.GUNPOWDER);
        addShapedRecipe(new ItemStack(ModBlocks.heimCore), "HHH", "HAH", "HHH", 'H', ModBlocks.heim, 'A', ModItems.aschGem);
    }

    private static void addShapedRecipe(ItemStack itemStack, Object... data) {
        CraftingManager.getInstance().addRecipe(itemStack, data);
    }

    private static void addShapelessRecipe(ItemStack result, ItemStack... components) {
        CraftingManager.getInstance().addShapelessRecipe(result, components);
    }
}
