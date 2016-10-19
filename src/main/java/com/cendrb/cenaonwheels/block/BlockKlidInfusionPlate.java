package com.cendrb.cenaonwheels.block;

import net.minecraft.block.material.Material;

/**
 * Created by cendr_000 on 18.10.2016.
 */
public class BlockKlidInfusionPlate extends BlockBase {

    private float efficiency;

    public BlockKlidInfusionPlate(String tierName, float efficiency) {
        super(Material.ROCK, "klidInfusion" + tierName + "Plate");
        this.efficiency = efficiency;
    }

    public float getEfficiency() {
        return efficiency;
    }
}
