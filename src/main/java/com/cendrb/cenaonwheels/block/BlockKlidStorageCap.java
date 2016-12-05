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
public class BlockKlidStorageCap extends BlockKlidStoragePart implements ITileEntityProvider {

    public BlockKlidStorageCap() {
        super(Material.ROCK, "klidStorageCap");
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean poweredInWorld = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityKlidStorage) {
            ((TileEntityKlidStorage) tileEntity).setTriggered(poweredInWorld);
        }
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
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityKlidStorage();
    }
}
