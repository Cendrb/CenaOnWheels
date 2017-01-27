package com.cendrb.cenaonwheels.block;

import com.cendrb.cenaonwheels.tileentity.TileEntityKlidStorage;
import com.cendrb.cenaonwheels.tileentity.TileEntityKlidStorageExtractor;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by cendr on 26/01/2017.
 */
public class BlockKlidStorageExtractor extends BlockKlidStoragePart implements ITileEntityProvider {
    public BlockKlidStorageExtractor() {
        super(Material.ROCK, "klidStorageExtractor");
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean poweredInWorld = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityKlidStorageExtractor) {
            ((TileEntityKlidStorageExtractor) tileEntity).setTriggered(poweredInWorld);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityKlidStorageExtractor();
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1);
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
