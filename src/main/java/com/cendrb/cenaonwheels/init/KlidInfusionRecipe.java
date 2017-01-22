package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.item.ItemChargeableTool;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cendr on 05/12/2016.
 */
public class KlidInfusionRecipe {
    private int requiredKlid;
    private final ItemStack result;
    private final Item[] ingredients;

    public KlidInfusionRecipe(int requiredKlid, ItemStack result, Item... ingredients) {
        this.requiredKlid = requiredKlid;
        this.result = result;
        this.ingredients = ingredients;
    }

    public KlidInfusionRecipeResult isCompatible(List<ItemStack> components) {
        ArrayList<Item> localComponents = new ArrayList<>(Arrays.asList(this.ingredients));
        for (ItemStack itemStack : components) {
            if(localComponents.contains(itemStack.getItem()))
                localComponents.remove(itemStack.getItem());
            else
                return KlidInfusionRecipeResult.Incompatible;
        }
        if(localComponents.isEmpty())
            return KlidInfusionRecipeResult.Complete;
        else
            return KlidInfusionRecipeResult.PartOfTheRecipe;
    }

    public ItemStack getResult() {
        return result;
    }

    public ItemStack getResultFor(List<ItemStack> ingredients)
    {
        if(ingredients.size() > 1)
            return getResult();
        else {
            ItemStack onlyIngredient = ingredients.get(0);
            if (onlyIngredient.getItem() instanceof ItemChargeableTool) {
                onlyIngredient.setItemDamage(0);
                return onlyIngredient;
            } else
                return getResult();
        }
    }

    public int getRequiredKlid() {
        return requiredKlid;
    }

    public Item[] getIngredients() {
        return ingredients;
    }

    public int getKlidAcceptanceThreshold()
    {
        return requiredKlid / 20;
    }
}
