package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.util.COWLogger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

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
            if (tileEntity != null && TileEntityMultiblockMaster.class.isAssignableFrom(tileEntity.getClass())) {
                ((TileEntityMultiblockMaster) tileEntity).checkMultiblock();
            } else
                COWLogger.logDebug("Request for check unsuccessful - no TileEntityMultiblockMaster found");
        }
    }

    public void setMasterPos(BlockPos masterPos) {
        this.masterPos = masterPos;
        if (!worldObj.isRemote) {
            IBlockState currentState = worldObj.getBlockState(pos);
            worldObj.notifyBlockUpdate(pos, currentState, currentState, 1);
        }
    }

    public BlockPos getMasterPos() {
        return masterPos;
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
        if (compound.hasKey("masterPos")) {
            int[] posArray = compound.getIntArray("masterPos");
            masterPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (masterPos != null)
            compound.setIntArray("masterPos", new int[]{masterPos.getX(), masterPos.getY(), masterPos.getZ()});
        return compound;
    }
}
