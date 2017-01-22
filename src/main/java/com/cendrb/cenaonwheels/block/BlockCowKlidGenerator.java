package com.cendrb.cenaonwheels.block;

import com.cendrb.cenaonwheels.tileentity.TileEntityCowKlidGenerator;
import com.cendrb.cenaonwheels.util.COWLogger;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by cendr_000 on 14.10.2016.
 */
public class BlockCowKlidGenerator extends BlockBase implements ITileEntityProvider {

    public static PropertyBool TRIGGERED = PropertyBool.create("triggered");

    public BlockCowKlidGenerator() {
        super(Material.ROCK, "cowKlidGenerator");
        setDefaultState(blockState.getBaseState().withProperty(TRIGGERED, false));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCowKlidGenerator();
    }

    public int tickRate(World worldIn) {
        return 4;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(TRIGGERED))
            return 1;
        else
            return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TRIGGERED, meta == 1);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TRIGGERED);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean poweredInWorld = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
        worldIn.setBlockState(pos, state.withProperty(TRIGGERED, poweredInWorld));

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityCowKlidGenerator) {
            ((TileEntityCowKlidGenerator) tileEntity).setTriggered(poweredInWorld);
        }
    }
}
