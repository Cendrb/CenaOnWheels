package com.cendrb.cenaonwheels.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.List;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class BlockTempusOre extends BlockOre {
    public BlockTempusOre() {
        super("tempusOre");
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return super.getExpDrop(state, world, pos, fortune);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return super.getDrops(world, pos, state, fortune);
    }
}
