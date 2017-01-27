package com.cendrb.cenaonwheels.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import scala.tools.nsc.transform.ExplicitOuter;

import java.util.List;
import java.util.Random;

/**
 * Created by cendr on 27/01/2017.
 */
public class CustomSoundExplosion extends Explosion {

    private Random random;

    private double explosionX;
    private double explosionY;
    private double explosionZ;
    private float explosionSize;
    private World worldObj;
    private boolean isSmoking;
    private boolean isFlaming;
    private SoundEvent explosionSound;

    public CustomSoundExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean smoking, SoundEvent explosionSound) {
        super(worldIn, entityIn, x, y, z, size, flaming, smoking);
        random = new Random();
        worldObj = worldIn;
        explosionX = x;
        explosionY = y;
        explosionZ = z;
        this.explosionSound = explosionSound;
        isFlaming = flaming;
        isSmoking = smoking;
    }

    @Override
    public void doExplosionB(boolean spawnParticles)
    {
        List<BlockPos> affectedBlockPositions = getAffectedBlockPositions();

        this.worldObj.playSound(null, this.explosionX, this.explosionY, this.explosionZ, explosionSound, SoundCategory.MASTER, 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

        if (this.explosionSize >= 2.0F && this.isSmoking)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
        }
        else
        {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
        }

        if (this.isSmoking)
        {
            for (BlockPos blockpos : affectedBlockPositions)
            {
                IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (spawnParticles)
                {
                    double d0 = (double)((float)blockpos.getX() + this.worldObj.rand.nextFloat());
                    double d1 = (double)((float)blockpos.getY() + this.worldObj.rand.nextFloat());
                    double d2 = (double)((float)blockpos.getZ() + this.worldObj.rand.nextFloat());
                    double d3 = d0 - this.explosionX;
                    double d4 = d1 - this.explosionY;
                    double d5 = d2 - this.explosionZ;
                    double d6 = (double) MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 = d3 / d6;
                    d4 = d4 / d6;
                    d5 = d5 / d6;
                    double d7 = 0.5D / (d6 / (double)this.explosionSize + 0.1D);
                    d7 = d7 * (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
                    d3 = d3 * d7;
                    d4 = d4 * d7;
                    d5 = d5 * d7;
                    this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.explosionX) / 2.0D, (d1 + this.explosionY) / 2.0D, (d2 + this.explosionZ) / 2.0D, d3, d4, d5);
                    this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5);
                }

                if (iblockstate.getMaterial() != Material.AIR)
                {
                    block.onBlockExploded(this.worldObj, blockpos, this);
                }
            }
        }

        if (this.isFlaming)
        {
            for (BlockPos blockpos1 : affectedBlockPositions)
            {
                if (this.worldObj.getBlockState(blockpos1).getMaterial() == Material.AIR && this.worldObj.getBlockState(blockpos1.down()).isFullBlock() && random.nextInt(3) == 0)
                {
                    this.worldObj.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }
}
