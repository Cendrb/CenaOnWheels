package com.cendrb.cenaonwheels.jei;

import com.cendrb.cenaonwheels.init.KlidInfusionRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr on 05/12/2016.
 */
public class KlidInfusionRecipeWrapper extends BlankRecipeWrapper {

    private KlidInfusionRecipe klidInfusionRecipe;

    public KlidInfusionRecipeWrapper(KlidInfusionRecipe klidInfusionRecipe)
    {
        this.klidInfusionRecipe = klidInfusionRecipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(ItemStack.class, klidInfusionRecipe.getResult());
        ArrayList<ItemStack> inputItemstacks = new ArrayList<>();
        for (Item item : klidInfusionRecipe.getIngredients()) {
            inputItemstacks.add(new ItemStack(item, 1));
        }
        ingredients.setInputs(ItemStack.class, inputItemstacks);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRendererObj.drawString(klidInfusionRecipe.getRequiredKlid() + " KLID", 12, 20, Color.GRAY.getRGB());
    }
}
