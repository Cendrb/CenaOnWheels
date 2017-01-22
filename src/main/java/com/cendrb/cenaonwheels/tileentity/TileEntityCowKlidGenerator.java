package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.ITargetable;
import com.cendrb.cenaonwheels.KlidWorldSavedData;
import com.cendrb.cenaonwheels.block.BlockCowKlidGenerator;
import com.cendrb.cenaonwheels.init.ModBlocks;
import com.cendrb.cenaonwheels.util.COWLogger;
import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.omg.CORBA.TRANSACTION_MODE;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by cendr_000 on 14.10.2016.
 */
public class TileEntityCowKlidGenerator extends TileEntity implements ITickable, ITargetable {

    private static final int BASE_PULSE_TICKS = 40;
    private static final int MAX_COWS = 20;

    private int ticksTillBurst = 0;
    private boolean triggered;

    private BlockPos targetLocation;

    private Random random = new Random();

    public TileEntityCowKlidGenerator() {
    }

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            if (triggered) {
                ticksTillBurst--;
                if (ticksTillBurst <= 0) {
                    // do a burst
                    ticksTillBurst = BASE_PULSE_TICKS;
                    int x1 = pos.getX() - 5;
                    int x2 = pos.getX() + 5;
                    int y1 = pos.getY() + 5;
                    int y2 = pos.getY() - 2;
                    int z1 = pos.getZ() + 5;
                    int z2 = pos.getZ() - 5;

                    List<EntityCow> cowsInRange = worldObj.getEntitiesWithinAABB(EntityCow.class, new AxisAlignedBB(
                            x1, y1, z1,
                            x2, y2, z2));
                    int cowsCount = cowsInRange.size();

                    // algorithm to automatically kill cows
                    int cowsToKill = cowsCount / (MAX_COWS / 2);
                    if(random.nextInt(MAX_COWS * 5) < cowsCount)
                    {
                        for(int i = 0; i < cowsToKill; i++)
                        {
                            cowsInRange.get(i).setDead();
                        }
                    }

                    int extractedKlidAmount = (int) (cowsCount * (random.nextFloat() + 0.5f));
                    if (extractedKlidAmount > 0)
                        if (targetLocation != null) {
                            WorldHelper.spawnKlidBurst(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, targetLocation, extractedKlidAmount);
                        } else {
                            WorldHelper.releaseKlidAt(worldObj, pos.getX(), pos.getY(), pos.getZ(), extractedKlidAmount);
                        }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("ticksTillBurst"))
            ticksTillBurst = compound.getInteger("ticksTillBurst");
        if (compound.hasKey("triggered"))
            triggered = compound.getBoolean("triggered");
        if (compound.hasKey("targetLocation"))
            targetLocation = BlockPos.fromLong(compound.getLong("targetLocation"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("ticksTillBurst", ticksTillBurst);
        compound.setBoolean("triggered", triggered);
        if (targetLocation != null)
            compound.setLong("targetLocation", targetLocation.toLong());
        return compound;
    }

    public boolean getTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    public void setTargetLocation(BlockPos pos) {
        targetLocation = pos;
    }

    public BlockPos getTargetLocation() {
        return targetLocation;
    }
}
