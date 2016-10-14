package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.VisStorageBlockEnergyValues;
import com.cendrb.cenaonwheels.block.BlockVisStorageCasing;
import com.cendrb.cenaonwheels.block.BlockVisStorageCore;
import com.cendrb.cenaonwheels.block.BlockVisStorageGlass;
import com.cendrb.cenaonwheels.util.COWLogger;
import com.cendrb.cenaonwheels.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr_000 on 02.10.2016.
 */
@SuppressWarnings("Duplicates")
public class TileEntityVisStorage extends TileEntityMultiblockMaster implements ITickable {

    private static final int TICKS_BETWEEN_CHECKS = 20;

    private boolean multiblockComplete = false;
    private int currentEnergyPerTick = 0;
    private int currentEnergyStored = 0;
    private int tickBurstThreshold = 5;

    private int tickCounter = 0;
    private int tickTimer = 0;

    public TileEntityVisStorage() {
        COWLogger.logDebug("TileEntityVisStorage ctor");
    }

    @Override
    public void update() {
        tickTimer++;
        if (tickTimer >= TICKS_BETWEEN_CHECKS) {
            tickTimer = 0;

            if (multiblockComplete) {
                if (!worldObj.isRemote) {
                    tickCounter++;
                    currentEnergyStored += currentEnergyPerTick;
                    COWLogger.logDebug("Storing vis at rate: " + currentEnergyPerTick + " Total stored = " + currentEnergyStored);
                    if (tickCounter >= tickBurstThreshold) {
                        COWLogger.logDebug("Time for shots fired! Vis burst incoming: " + currentEnergyStored);
                        tickCounter = 0;
                        currentEnergyStored = 0;
                    }
                }
            } else
                checkMultiblock();
        }
    }

    @Override
    public void checkMultiblock() {
        COWLogger.logDebug("Checking multiblock...");

        // gets automatically called by TileEntityMultiblockParts
        multiblockComplete = false;
        currentEnergyPerTick = 0;

        boolean success = true;
        ArrayList<BlockPos> blockPositions = new ArrayList<>();

        // check casings bellow
        for (int westOff = -1; westOff < 2; westOff++)
            for (int northOff = -1; northOff < 2; northOff++) {
                if (westOff != 0 || northOff != 0) {
                    // when not the center block
                    BlockPos calculatedPos = pos.down().west(westOff).north(northOff);
                    if (WorldHelper.isBlock(worldObj, calculatedPos, BlockVisStorageCasing.class)) {
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
                if (WorldHelper.isBlock(worldObj, calculatedPos, BlockVisStorageCore.class))
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
                                    currentEnergyPerTick += energyValue;
                            }
                }
                if (currentEnergyPerTick > 0) {
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
    }

    private boolean check5WallCasings(EnumFacing direction, List<BlockPos> listToAddBlockPosTo, BlockPos middlePos) {
        for (int off = -2; off < 3; off++) {
            BlockPos calculatedPos = middlePos.offset(direction, off);
            if (off == -2 || off == 2) {
                if (WorldHelper.isBlock(worldObj, calculatedPos, BlockVisStorageCasing.class)) {
                    listToAddBlockPosTo.add(calculatedPos);
                } else
                    return false;
            } else {
                if (WorldHelper.isBlock(worldObj, calculatedPos, BlockVisStorageGlass.class)) {
                    listToAddBlockPosTo.add(calculatedPos);
                } else
                    return false;
            }
        }
        return true;
    }
}
