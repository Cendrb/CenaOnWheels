package com.cendrb.cenaonwheels.block;

import net.minecraft.block.material.Material;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class BlockOre extends BlockBase {
    public BlockOre(String pureName) {
        super(Material.ROCK, pureName);
        setHardness(3f);
        setResistance(5f);
    }
}
