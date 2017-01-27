package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.ITargetable;
import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 * Created by cendr on 27/01/2017.
 */
public class TileEntityKlidStorageExtractor extends TileEntityMultiblockPart implements ITickable, ITargetable {

    private static final int TICKS_BETWEEN_BURSTS = 60;

    private int tickTimer = 0;

    private boolean triggered;
    private BlockPos targetLocation;

    @Override
    public void update() {
        if (!worldObj.isRemote && triggered && targetLocation != null) {
            if (tickTimer >= TICKS_BETWEEN_BURSTS) {
                TileEntityKlidStorage masterKlidStorage = (TileEntityKlidStorage) worldObj.getTileEntity(getMasterPos());
                if (masterKlidStorage != null && masterKlidStorage.isMultiblockComplete()) {
                    int toBeReleased;
                    if (masterKlidStorage.getCurrentEnergy() < masterKlidStorage.getOutputBurstVolume()) {
                        toBeReleased = masterKlidStorage.getCurrentEnergy();
                    } else {
                        toBeReleased = masterKlidStorage.getOutputBurstVolume();
                    }
                    if (toBeReleased > 0) {
                        WorldHelper.spawnKlidBurst(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, targetLocation, toBeReleased);
                    }
                }
                tickTimer = 0;
            }
            tickTimer++;
        } else {
            tickTimer = 0;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (targetLocation != null)
            compound.setLong("targetLocation", targetLocation.toLong());
        compound.setBoolean("triggered", triggered);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("targetLocation"))
            targetLocation = BlockPos.fromLong(compound.getLong("targetLocation"));
        if (compound.hasKey("triggered"))
            triggered = compound.getBoolean("triggered");
    }

    @Override
    public void setTargetLocation(BlockPos targetLocation) {
        this.targetLocation = targetLocation;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }
}
