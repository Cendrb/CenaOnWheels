package com.cendrb.cenaonwheels.init;

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
    private final Item[] components;

    public KlidInfusionRecipe(int requiredKlid, ItemStack result, Item... components) {
        this.requiredKlid = requiredKlid;
        this.result = result;
        this.components = components;
    }

    public KlidInfusionRecipeResult isCompatible(List<Item> components) {
        ArrayList<Item> localComponents = new ArrayList<>(Arrays.asList(this.components));
        for (Item item : components) {
            if(localComponents.contains(item))
                localComponents.remove(item);
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

    public int getRequiredKlid() {
        return requiredKlid;
    }
}
