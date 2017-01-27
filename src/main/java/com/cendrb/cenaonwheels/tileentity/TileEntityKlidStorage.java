package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.IKlidAcceptor;
import com.cendrb.cenaonwheels.ITargetable;
import com.cendrb.cenaonwheels.KlidWorldSavedData;
import com.cendrb.cenaonwheels.KlidStorageBlockEnergyValues;
import com.cendrb.cenaonwheels.block.BlockKlidStorageCasing;
import com.cendrb.cenaonwheels.block.BlockKlidStorageCore;
import com.cendrb.cenaonwheels.block.BlockKlidStorageExtractor;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr_000 on 02.10.2016.
 */
@SuppressWarnings("Duplicates")
public class TileEntityKlidStorage extends TileEntityMultiblockMaster implements ITickable, IKlidAcceptor {

    private static final int TICKS_BETWEEN_CHECKS = 20;

    private boolean multiblockComplete = false;
    private int currentEnergyMax = 0;
    private int currentEnergy = 0;
    private int outputBurstVolume = 0;

    private int tickTimer = 0;

    public TileEntityKlidStorage() {

    }

    @Override
    public void update() {
        tickTimer++;
        if (tickTimer >= TICKS_BETWEEN_CHECKS) {
            tickTimer = 0;
            if (!multiblockComplete) {
                checkMultiblock();
            }
        }
    }

    @Override
    public void acceptKlid(int amount) {
        currentEnergy += amount;
        COWLogger.logDebug("Accepted " + amount + " klid! TOTAL: " + currentEnergy);
        if (currentEnergy + amount > currentEnergyMax) {
            WorldHelper.releaseKlidAt(worldObj, pos.getX(), pos.getY(), pos.getZ(), amount - (currentEnergyMax - currentEnergy));
            currentEnergy = currentEnergyMax;
        }
    }

    @Override
    public void checkMultiblock() {
        // gets automatically called by TileEntityMultiblockParts
        multiblockComplete = false;
        currentEnergyMax = 0;
        outputBurstVolume = 0;

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

        // check for klid extractors at the top
        ArrayList<BlockPos> extractorPossiblePositions = new ArrayList<>();
        extractorPossiblePositions.add(pos.west());
        extractorPossiblePositions.add(pos.east());
        extractorPossiblePositions.add(pos.north());
        extractorPossiblePositions.add(pos.south());
        extractorPossiblePositions.add(pos.north().west());
        extractorPossiblePositions.add(pos.north().east());
        extractorPossiblePositions.add(pos.south().west());
        extractorPossiblePositions.add(pos.south().east());
        for (BlockPos extractorBlockPos : extractorPossiblePositions) {
            if (WorldHelper.isBlock(worldObj, extractorBlockPos, BlockKlidStorageExtractor.class)) {
                blockPositions.add(extractorBlockPos);
            }
        }

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
                if (WorldHelper.isBlock(worldObj, calculatedPos, BlockKlidStorageCore.class)) {
                    outputBurstVolume += ((BlockKlidStorageCore) worldObj.getBlockState(calculatedPos).getBlock()).getValue();
                    blockPositions.add(calculatedPos);
                } else
                    success = false;
            }

            if (success) {
                for (int yOff = 2; yOff < successfulLayers + 2; yOff++) {
                    for (int xOff = -1; xOff < 2; xOff++)
                        for (int zOff = -1; zOff < 2; zOff++)
                            if (xOff != 0 || zOff != 0) { // do not count cores - do not execute when both coords are 0
                                IBlockState blockState = worldObj.getBlockState(pos.down(yOff).west(xOff).north(zOff));
                                Integer energyValue = KlidStorageBlockEnergyValues.getEnergyValue(blockState.getBlock());
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
        if (compound.hasKey("currentEnergy"))
            currentEnergy = compound.getInteger("currentEnergy");
        if (compound.hasKey("outputBurstVolume"))
            outputBurstVolume = compound.getInteger("outputBurstVolume");

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("currentEnergy", currentEnergy);
        compound.setInteger("outputBurstVolume", outputBurstVolume);
        return compound;
    }

    /**
     * Return an appropriate bounding box enclosing the TESR
     * This method is used to control whether the TESR should be rendered or not, depending on where the player is looking.
     * The default is the AABB for the parent block, which might be too small if the TESR renders outside the borders of the
     * parent block.
     * If you get the boundary too small, the TESR may disappear when you aren't looking directly at it.
     *
     * @return an appropriately size AABB for the TileEntity
     */
    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
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

    public int getOutputBurstVolume() {
        return outputBurstVolume;
    }
}
