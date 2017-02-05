package com.cendrb.cenaonwheels;

import com.cendrb.cenaonwheels.network.SyncEntityNBTMessage;
import com.cendrb.cenaonwheels.network.SyncWorldKlidToClientMessage;
import com.cendrb.cenaonwheels.util.COWLogger;
import me.guichaguri.tickratechanger.api.TickrateAPI;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by cendr_000 on 14.10.2016.
 */
public class KlidWorldSavedData extends WorldSavedData {

    public static final int KLID_TIME_SLOW_THRESHOLD = 10000;

    private static final int DEFAULT_TICKRATE = 20;
    private static final int MINIMAL_TICKRATE = 5;

    private int klidInTheAtmosphere = 0;

    public KlidWorldSavedData(String name) {
        super(name);
    }

    public KlidWorldSavedData() {
        super(RefStrings.MODID);
    }

    public static KlidWorldSavedData getFor(World world) {
        KlidWorldSavedData data = (KlidWorldSavedData) world.getPerWorldStorage().getOrLoadData(KlidWorldSavedData.class, RefStrings.MODID);
        if (data == null) {
            data = new KlidWorldSavedData();
            world.getPerWorldStorage().setData(RefStrings.MODID, data);
        }
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        klidInTheAtmosphere = nbt.getInteger("klidInTheAtmosphere");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("klidInTheAtmosphere", klidInTheAtmosphere);
        return tag;
    }


    @Override
    public void deserializeNBT(NBTTagCompound p_deserializeNBT_1_) {
        readFromNBT(p_deserializeNBT_1_);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return writeToNBT(new NBTTagCompound());
    }

    public int getKlidInTheAtmosphere() {
        return klidInTheAtmosphere;
    }

    public void setKlidInTheAtmosphere(int klidInTheAtmosphere, boolean isServer) {
        this.klidInTheAtmosphere = klidInTheAtmosphere;
        markDirty();
        if (isServer) {
            sendCurrentValuesToAllClients();
        }
        COWLogger.logDebug("BEWARE: Klid levels are rising... " + klidInTheAtmosphere);
        refreshCurrentTickRate(isServer);
    }

    public void sendCurrentValuesToAllClients()
    {
        Core.networkWrapper.sendToAll(new SyncWorldKlidToClientMessage(klidInTheAtmosphere));
    }

    private void refreshCurrentTickRate(boolean isServer) {
        if (klidInTheAtmosphere > KLID_TIME_SLOW_THRESHOLD) {
            int calculatedTickRate = DEFAULT_TICKRATE - (klidInTheAtmosphere - KLID_TIME_SLOW_THRESHOLD) / 100;
            if (calculatedTickRate < MINIMAL_TICKRATE)
                calculatedTickRate = MINIMAL_TICKRATE;
            if (isServer && (int) TickrateAPI.getServerTickrate() != calculatedTickRate) {
                TickrateAPI.changeTickrate(calculatedTickRate, true);
            }
        }
    }
}
