package com.cendrb.cenaonwheels.init;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
        recipes.add(new KlidInfusionRecipe(10000, new ItemStack(ModItems.klidGem, 1), ModItems.tempusShard, Items.ENDER_PEARL, Items.DIAMOND));
    }

    public static KlidInfusionRecipeResult isPartOfRecipe(List<Item> items) {
        boolean recipeFound = false;
        for (KlidInfusionRecipe recipe : recipes) {
            KlidInfusionRecipeResult result = recipe.isCompatible(items);
            if(result == KlidInfusionRecipeResult.Complete)
                return KlidInfusionRecipeResult.Complete;
            else if(result == KlidInfusionRecipeResult.PartOfTheRecipe)
                recipeFound = true;
        }
        if(recipeFound)
            return KlidInfusionRecipeResult.PartOfTheRecipe;
        else
            return KlidInfusionRecipeResult.Incompatible;
    }

    public static KlidInfusionRecipe getRecipeFor(List<Item> items)
    {
        for (KlidInfusionRecipe recipe : recipes) {
            KlidInfusionRecipeResult result = recipe.isCompatible(items);
            if(result == KlidInfusionRecipeResult.Complete)
                return recipe;
        }
        return null;
    }

    public static ArrayList<KlidInfusionRecipe> getRecipes() {
        return recipes;
    }
}
