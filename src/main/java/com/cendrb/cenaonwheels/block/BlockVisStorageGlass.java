package com.cendrb.cenaonwheels.block;

import com.cendrb.cenaonwheels.tileentity.TileEntityMultiblockPart;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class BlockVisStorageGlass extends BlockMultiblockPart {
    public BlockVisStorageGlass() {
        super(Material.ROCK, "visStorageGlass");
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
