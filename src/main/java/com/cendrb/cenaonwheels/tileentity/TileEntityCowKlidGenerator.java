package com.cendrb.cenaonwheels.tileentity;

import com.cendrb.cenaonwheels.Core;
import com.cendrb.cenaonwheels.KlidWorldSavedData;
import com.cendrb.cenaonwheels.entity.EntityKlidBurst;
import com.cendrb.cenaonwheels.init.ModBlocks;
import com.cendrb.cenaonwheels.network.SyncEntityNBTMessage;
import com.cendrb.cenaonwheels.util.COWLogger;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by cendr_000 on 14.10.2016.
 */
public class TileEntityCowKlidGenerator extends TileEntity implements ITickable {

    private static final int BASE_PULSE_TICKS = 40;

    private int ticksTillBurst = 0;
    private boolean triggered;
    private BlockPos targetLocation;

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
                    List<EntityCow> cowsInRange = worldObj.getEntitiesWithinAABB(EntityCow.class, new AxisAlignedBB(
                            pos.getX() - 10, getPos().getY() - 5, pos.getZ() - 10,
                            pos.getX() + 10, pos.getY() + 5, pos.getZ() + 10));
                    int cowsCount = cowsInRange.size();
                    COWLogger.logDebug(cowsCount + " cows found!");
                    if (targetLocation != null) {
                        COWLogger.logDebug("Storage found");
                        EntityKlidBurst klidBurst = new EntityKlidBurst(worldObj);
                        klidBurst.setPosition(pos.getX(), pos.getY() + 2, pos.getZ());
                        klidBurst.setTarget(targetLocation);
                        klidBurst.setValue(cowsCount);
                        worldObj.spawnEntityInWorld(klidBurst);
                        Core.networkWrapper.sendToAllAround(new SyncEntityNBTMessage(klidBurst.getEntityId(), klidBurst.serializeNBT()), new NetworkRegistry.TargetPoint(0, pos.getX(), pos.getY(), pos.getZ(), 64));
                    } else {
                        COWLogger.logDebug("No storage found, releasing klid into the atmosphere");
                        KlidWorldSavedData savedData = KlidWorldSavedData.getFor(worldObj);
                        savedData.setKlidInTheAtmosphere(savedData.getKlidInTheAtmosphere() + cowsCount);
                    }
                }
            }
        }
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
        ticksTillBurst = compound.getInteger("ticksTillBurst");
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
        markDirty();
        worldObj.notifyBlockOfStateChange(pos, ModBlocks.cowKlidGenerator);
    }

    public void setTargetLocation(BlockPos pos) {
        targetLocation = pos;
    }

}
