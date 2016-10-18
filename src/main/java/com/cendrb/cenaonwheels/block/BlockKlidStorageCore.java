package com.cendrb.cenaonwheels.block;

import net.minecraft.block.material.Material;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class BlockKlidStorageCore extends BlockMultiblockPart {

    private int value;

    public BlockKlidStorageCore(String tierName, int value) {
        super(Material.ROCK, "klidStorage" + tierName + "Core");
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
