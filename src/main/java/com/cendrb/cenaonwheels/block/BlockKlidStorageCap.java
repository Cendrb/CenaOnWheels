package com.cendrb.cenaonwheels.block;

import com.cendrb.cenaonwheels.tileentity.TileEntityCowKlidGenerator;
import com.cendrb.cenaonwheels.tileentity.TileEntityKlidStorage;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class BlockKlidStorageCap extends BlockMultiblockMaster implements ITileEntityProvider {

    public BlockKlidStorageCap() {
        super(Material.ROCK, "klidStorageCap");
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
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntityKlidStorage klidStorage = (TileEntityKlidStorage) worldIn.getTileEntity(pos);
        if (klidStorage != null) {
            return klidStorage.getComparatorOutput();
        }
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityKlidStorage();
    }
}
