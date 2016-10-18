package com.cendrb.cenaonwheels.eventhandlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by cendr_000 on 14.10.2016.
 */
public class EntityStruckByLightningHandler {

    @SubscribeEvent
    public void onEntityStruckByLightning(EntityStruckByLightningEvent entityStruckByLightningEvent)
    {
        Entity struckEntity = entityStruckByLightningEvent.getEntity();
        if(struckEntity instanceof EntityCow)
        {
            (struckEntity).addVelocity(0, 10, 0);
        }
    }
}
