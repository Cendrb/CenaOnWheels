package com.cendrb.cenaonwheels.jei;

import com.cendrb.cenaonwheels.init.ModBlocks;
import com.cendrb.cenaonwheels.init.ModItems;
import com.cendrb.cenaonwheels.init.ModKlidInfusionRecipes;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

/**
 * Created by cendr on 05/12/2016.
 */
@mezz.jei.api.JEIPlugin
public class JEIPlugin extends BlankModPlugin {

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registry.addRecipeCategories(new KlidInfusionRecipeCategory(guiHelper));

        registry.addRecipeHandlers(new KlidInfusionRecipeHandler());

        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.klidInfusionBasicPlate), KlidInfusionRecipeCategory.UUID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.klidInfusionAdvancedPlate), KlidInfusionRecipeCategory.UUID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.klidInfusionUltimatePlate), KlidInfusionRecipeCategory.UUID);

        registry.addRecipes(ModKlidInfusionRecipes.getRecipes());

    }
}
