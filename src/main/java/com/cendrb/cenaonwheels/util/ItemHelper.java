package com.cendrb.cenaonwheels.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by cendr on 05/02/2017.
 */
public class ItemHelper {
    public static boolean isItem(@Nullable ItemStack itemStack, Item item) {
        if (itemStack == null) {
            return false;
        } else {
            return itemStack.getItem() == item;
        }
    }
}
