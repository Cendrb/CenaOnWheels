package com.cendrb.cenaonwheels.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr on 05/12/2016.
 */
public class ModKlidInfusionRecipes {

    private static ArrayList<KlidInfusionRecipe> recipes;

    public static void init() {
        recipes = new ArrayList<>();
        recipes.add(new KlidInfusionRecipe(1000, new ItemStack(ModItems.klidMetal, 1), ModItems.tempusShard, Items.IRON_INGOT, Items.GOLD_INGOT));
        recipes.add(new KlidInfusionRecipe(5000, new ItemStack(ModItems.klidGem, 1), ModItems.tempusShard, Items.ENDER_PEARL, Items.DIAMOND));
        recipes.add(new KlidInfusionRecipe(10000, new ItemStack(ModItems.aschGem, 1), ModItems.klidGem, Item.getItemFromBlock(ModBlocks.klidMetalBlock), ModItems.klidGem));
        recipes.add(new KlidInfusionRecipe(500, new ItemStack(ModItems.timelessGunpowder, 2), Items.GUNPOWDER, ModItems.tempusShard, Items.GUNPOWDER));
        recipes.add(new KlidInfusionRecipe(500, new ItemStack(ModBlocks.aschSapling), ModItems.tempusShard, Item.getItemFromBlock(Blocks.SAPLING), ModItems.tempusShard));
    }

    public static KlidInfusionRecipeResult isPartOfRecipe(List<ItemStack> items) {
        boolean recipeFound = false;
        for (KlidInfusionRecipe recipe : recipes) {
            KlidInfusionRecipeResult result = recipe.isCompatible(items);
            if (result == KlidInfusionRecipeResult.Complete)
                return KlidInfusionRecipeResult.Complete;
            else if (result == KlidInfusionRecipeResult.PartOfTheRecipe)
                recipeFound = true;
        }
        if (recipeFound)
            return KlidInfusionRecipeResult.PartOfTheRecipe;
        else
            return KlidInfusionRecipeResult.Incompatible;
    }

    public static KlidInfusionRecipe getRecipeFor(List<ItemStack> items) {
        for (KlidInfusionRecipe recipe : recipes) {
            KlidInfusionRecipeResult result = recipe.isCompatible(items);
            if (result == KlidInfusionRecipeResult.Complete)
                return recipe;
        }
        return null;
    }

    public static ArrayList<KlidInfusionRecipe> getRecipes() {
        return recipes;
    }
}
