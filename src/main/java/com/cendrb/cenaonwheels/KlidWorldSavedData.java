package com.cendrb.cenaonwheels;

import com.cendrb.cenaonwheels.util.COWLogger;
import me.guichaguri.tickratechanger.api.TickrateAPI;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

/**
 * Created by cendr_000 on 14.10.2016.
 */
public class KlidWorldSavedData extends WorldSavedData {

    private static int DEFAULT_TICKRATE = 20;
    private static int MINIMAL_TICKRATE = 5;

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

    public int getKlidInTheAtmosphere() {
        return klidInTheAtmosphere;
    }

    public void setKlidInTheAtmosphere(int klidInTheAtmosphere) {
        this.klidInTheAtmosphere = klidInTheAtmosphere;
        markDirty();
        COWLogger.logDebug("BEWARE: Klid levels are rising... " + klidInTheAtmosphere);
        refreshCurrentTickRate();
    }

    public void refreshCurrentTickRate() {
        if (klidInTheAtmosphere > 100000) {
            int calculatedTickRate = DEFAULT_TICKRATE - (klidInTheAtmosphere - 1000) / 100;
            if (calculatedTickRate < MINIMAL_TICKRATE)
                calculatedTickRate = MINIMAL_TICKRATE;
            if ((int)TickrateAPI.getServerTickrate() != calculatedTickRate) {
                TickrateAPI.changeMapTickrate(calculatedTickRate);
                TickrateAPI.changeTickrate(calculatedTickRate);
            }
        }
    }
}
