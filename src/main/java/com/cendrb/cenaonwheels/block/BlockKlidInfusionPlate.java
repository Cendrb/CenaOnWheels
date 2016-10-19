package com.cendrb.cenaonwheels.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

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

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
