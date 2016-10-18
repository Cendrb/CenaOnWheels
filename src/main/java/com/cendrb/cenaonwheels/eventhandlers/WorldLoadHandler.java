package com.cendrb.cenaonwheels.eventhandlers;

import com.cendrb.cenaonwheels.KlidWorldSavedData;
import com.cendrb.cenaonwheels.util.COWLogger;
import me.guichaguri.tickratechanger.api.TickrateAPI;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by cendr_000 on 14.10.2016.
 */
public class WorldLoadHandler {

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load worldLoadEvent)
    {
        COWLogger.logDebug("World got loaded");
        KlidWorldSavedData klidWorldSavedData = KlidWorldSavedData.getFor(worldLoadEvent.getWorld());
        klidWorldSavedData.refreshCurrentTickRate();
    }
}
