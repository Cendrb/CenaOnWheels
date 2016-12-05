package com.cendrb.cenaonwheels.jei;

import com.cendrb.cenaonwheels.RefStrings;
import com.cendrb.cenaonwheels.init.KlidInfusionRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * Created by cendr on 05/12/2016.
 */

public class KlidInfusionRecipeCategory extends BlankRecipeCategory<KlidInfusionRecipeWrapper> {

    public static final String UUID = "klidinfusion";

    private static final int infusionOutputSlot = 0;
    private static final int infusionInputSlot1 = 1;
    private static final int infusionInputSlot2 = 2;
    private static final int infusionInputSlot3 = 3;

    private final IDrawable background;
    private final IDrawable icon;

    public KlidInfusionRecipeCategory(IGuiHelper guiHelper)
    {
        ResourceLocation backgroundResourceLocation = new ResourceLocation(RefStrings.MODID, "textures/jei/klidInfusion.png");
        background = guiHelper.createDrawable(backgroundResourceLocation, 0, 0, 175, 165);
        icon = guiHelper.createDrawable(backgroundResourceLocation, 176, 0, 55, 65);
    }

    @Override
    public String getUid() {
        return UUID;
    }

    @Override
    public String getTitle() {
        return "Klid Infusion";
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void drawAnimations(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, KlidInfusionRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(infusionOutputSlot, false, 78, 16);
        guiItemStacks.init(infusionInputSlot1, true, 55, 50);
        guiItemStacks.init(infusionInputSlot2, true, 78, 57);
        guiItemStacks.init(infusionInputSlot3, true, 101, 50);

        guiItemStacks.set(infusionOutputSlot, ingredients.getOutputs(ItemStack.class).get(0));
        guiItemStacks.set(infusionInputSlot1, ingredients.getInputs(ItemStack.class).get(0));
        guiItemStacks.set(infusionInputSlot2, ingredients.getInputs(ItemStack.class).get(1));
        guiItemStacks.set(infusionInputSlot3, ingredients.getInputs(ItemStack.class).get(2));
    }
}
