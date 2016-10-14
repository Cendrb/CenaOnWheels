package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.util.COWLogger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class TileEntityMultiblockPart extends TileEntity {

    private BlockPos masterPos = null;

    public TileEntityMultiblockPart() {
    }

    public void requestMultiblockCheck() {
        if (masterPos != null) {
            TileEntity tileEntity = worldObj.getTileEntity(masterPos);
            if (tileEntity != null && TileEntityMultiblockMaster.class.isAssignableFrom(tileEntity.getClass()))
            {
                COWLogger.logDebug("Requesting multiblock check...");
                ((TileEntityMultiblockMaster) tileEntity).checkMultiblock();
            }
            else
                COWLogger.logDebug("Request for check unsuccessful - no TileEntityMultiblockMaster found");
        }
    }

    public void setMasterPos(BlockPos masterPos) {
        this.masterPos = masterPos;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("masterPos")) {
            int[] posArray = compound.getIntArray("masterPos");
            masterPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (masterPos != null)
            compound.setIntArray("masterPos", new int[]{masterPos.getX(), masterPos.getY(), masterPos.getZ()});
        return compound;
    }
}
