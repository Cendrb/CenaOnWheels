package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.KlidWorldSavedData;
import com.cendrb.cenaonwheels.VisStorageBlockEnergyValues;
import com.cendrb.cenaonwheels.block.BlockKlidStorageCasing;
import com.cendrb.cenaonwheels.block.BlockKlidStorageCore;
import com.cendrb.cenaonwheels.block.BlockKlidStorageGlass;
import com.cendrb.cenaonwheels.util.COWLogger;
import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr_000 on 02.10.2016.
 */
@SuppressWarnings("Duplicates")
public class TileEntityKlidStorage extends TileEntityMultiblockMaster implements ITickable {

    private static final int TICKS_BETWEEN_CHECKS = 20;

    private boolean multiblockComplete = false;
    private int currentEnergyMax = 0;
    private int currentEnergy = 0;

    private int tickTimer = 0;

    public TileEntityKlidStorage() {

    }

    @Override
    public void update() {
        tickTimer++;
        if (tickTimer >= TICKS_BETWEEN_CHECKS) {
            tickTimer = 0;

            if (multiblockComplete) {
                // do something?
            } else
                checkMultiblock();
        }
    }

    public void acceptEnergy(int energy) {
        COWLogger.logDebug("Accepting " + energy + " COW energy!");
        currentEnergy += energy;
        if (currentEnergy + energy > currentEnergyMax) {
            currentEnergy = currentEnergyMax;
            COWLogger.logDebug("Vis overflow!");
            if (!worldObj.isRemote) {
                KlidWorldSavedData savedData = KlidWorldSavedData.getFor(worldObj);
                savedData.setKlidInTheAtmosphere(savedData.getKlidInTheAtmosphere() + energy);
            }
        }
        COWLogger.logDebug("Total energy: " + currentEnergy);
        markDirty();
    }

    @Override
    public void checkMultiblock() {
        COWLogger.logDebug("Checking multiblock...");

        // gets automatically called by TileEntityMultiblockParts
        multiblockComplete = false;
        currentEnergyMax = 0;

        boolean success = true;
        ArrayList<BlockPos> blockPositions = new ArrayList<>();

        // check casings bellow
        for (int westOff = -1; westOff < 2; westOff++)
            for (int northOff = -1; northOff < 2; northOff++) {
                if (westOff != 0 || northOff != 0) {
                    // when not the center block
                    BlockPos calculatedPos = pos.down().west(westOff).north(northOff);
                    if (WorldHelper.isBlock(worldObj, calculatedPos, BlockKlidStorageCasing.class)) {
                        blockPositions.add(calculatedPos);
                    } else {
                        success = false;
                    }
                }
            }

        if (!success)
            return;

        // check "the tunnel" casings
        boolean allCasingsFound = true;
        int successfulLayers = 0;
        for (int yOff = 2; allCasingsFound; yOff++) {
            allCasingsFound = check5WallCasings(EnumFacing.EAST, blockPositions, pos.down(yOff).north(2)) && allCasingsFound;
            allCasingsFound = check5WallCasings(EnumFacing.EAST, blockPositions, pos.down(yOff).south(2)) && allCasingsFound;
            allCasingsFound = check5WallCasings(EnumFacing.NORTH, blockPositions, pos.down(yOff).west(2)) && allCasingsFound;
            allCasingsFound = check5WallCasings(EnumFacing.NORTH, blockPositions, pos.down(yOff).east(2)) && allCasingsFound;

            if (allCasingsFound)
                successfulLayers++;
        }

        if (successfulLayers > 0) {
            // check the cores
            for (int yOff = 1; yOff < successfulLayers + 2; yOff++) {
                BlockPos calculatedPos = pos.down(yOff);
                if (WorldHelper.isBlock(worldObj, calculatedPos, BlockKlidStorageCore.class))
                    blockPositions.add(calculatedPos);
                else
                    success = false;
            }

            if (success) {
                for (int yOff = 2; yOff < successfulLayers + 2; yOff++) {
                    for (int xOff = -1; xOff < 2; xOff++)
                        for (int zOff = -1; zOff < 2; zOff++)
                            if (xOff != 0 || zOff != 0) { // do not count cores - do not execute when both coords are 0
                                IBlockState blockState = worldObj.getBlockState(pos.down(yOff).west(xOff).north(zOff));
                                Integer energyValue = VisStorageBlockEnergyValues.getEnergyValue(blockState.getBlock());
                                if (energyValue != null)
                                    currentEnergyMax += energyValue;
                            }
                }
                if (currentEnergyMax > 0) {
                    multiblockComplete = true;
                    for (BlockPos pos : blockPositions) {
                        TileEntity tileEntity = worldObj.getTileEntity(pos);
                        if (tileEntity != null && tileEntity instanceof TileEntityMultiblockPart) {
                            // set the master to the parts so they can notify
                            ((TileEntityMultiblockPart) tileEntity).setMasterPos(this.pos);
                        }
                    }
                }
            }
        }
        if (!multiblockComplete && currentEnergy > 0) {
            KlidWorldSavedData savedData = KlidWorldSavedData.getFor(worldObj);
            savedData.setKlidInTheAtmosphere(savedData.getKlidInTheAtmosphere() + currentEnergy);
            currentEnergy = 0;
            EntityLightningBolt lightningBolt = new EntityLightningBolt(worldObj, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, false);
            worldObj.spawnEntityInWorld(lightningBolt);
        }
    }

    private boolean check5WallCasings(EnumFacing direction, List<BlockPos> listToAddBlockPosTo, BlockPos middlePos) {
        for (int off = -2; off < 3; off++) {
            BlockPos calculatedPos = middlePos.offset(direction, off);
            if (off == -2 || off == 2) {
                if (WorldHelper.isBlock(worldObj, calculatedPos, BlockKlidStorageCasing.class)) {
                    listToAddBlockPosTo.add(calculatedPos);
                } else
                    return false;
            } else {
                if (WorldHelper.isBlock(worldObj, calculatedPos, BlockKlidStorageGlass.class)) {
                    listToAddBlockPosTo.add(calculatedPos);
                } else
                    return false;
            }
        }
        return true;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        int metadata = getBlockMetadata();
        return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        currentEnergy = compound.getInteger("currentEnergy");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("currentEnergy", currentEnergy);
        return compound;
    }

    public boolean isMultiblockComplete() {
        return multiblockComplete;
    }

    public int getCurrentEnergyMax() {
        return currentEnergyMax;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }
}