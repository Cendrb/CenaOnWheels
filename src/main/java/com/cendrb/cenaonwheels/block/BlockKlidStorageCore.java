package com.cendrb.cenaonwheels.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class BlockKlidStorageCore extends BlockKlidStoragePart {

    private int value;

    public BlockKlidStorageCore(String tierName, int value) {
        super(Material.ROCK, "klidStorage" + tierName + "Core");
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        tooltip.add("Klid per burst (see JEI): " + value);
        super.addInformation(stack, player, tooltip, advanced);
    }
}
