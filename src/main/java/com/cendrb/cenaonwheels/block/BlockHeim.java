package com.cendrb.cenaonwheels.block;

import com.cendrb.cenaonwheels.tileentity.TileEntityHeimCore;
import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by cendr_000 on 3. 7. 2015.
 */
public class BlockHeim extends BlockBase {

    public BlockHeim() {
        super(Material.ROCK, "heim");
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        entityIn.motionY = 1.0F;

        WorldHelper.createHeimExplosion(worldIn, null, pos.getX(), pos.getY(), pos.getZ(), 5f);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        WorldHelper.createHeimExplosion(worldIn, null, pos.getX(), pos.getY(), pos.getZ(), 5f);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.getBlockState(pos.down()).getBlock() instanceof BlockHeimCore) {
            BlockHeimCore.setupMultiblockAkbar(worldIn, pos.down());
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return new AxisAlignedBB(pos.add(0.1, 0.1, 0.1), pos.add(0.9, 0.9, 0.9));
    }
}
