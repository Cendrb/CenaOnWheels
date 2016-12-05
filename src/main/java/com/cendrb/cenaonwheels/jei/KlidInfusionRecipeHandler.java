package com.cendrb.cenaonwheels.jei;

import com.cendrb.cenaonwheels.init.KlidInfusionRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Created by cendr on 05/12/2016.
 */
public class KlidInfusionRecipeHandler implements IRecipeHandler<KlidInfusionRecipe> {
    @Override
    public Class<KlidInfusionRecipe> getRecipeClass() {
        return KlidInfusionRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return KlidInfusionRecipeCategory.UUID;
    }

    @Override
    public String getRecipeCategoryUid(KlidInfusionRecipe recipe) {
        return KlidInfusionRecipeCategory.UUID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(KlidInfusionRecipe recipe) {
        return new KlidInfusionRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(KlidInfusionRecipe recipe) {
        return true;
    }
}
