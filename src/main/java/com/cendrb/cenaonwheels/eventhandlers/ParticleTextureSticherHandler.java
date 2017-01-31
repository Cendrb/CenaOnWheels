package com.cendrb.cenaonwheels.eventhandlers;

import com.cendrb.cenaonwheels.RefStrings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by cendr on 30/01/2017.
 */
public class ParticleTextureSticherHandler {
    @SubscribeEvent
    public void stitcherEventPre(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(new ResourceLocation(RefStrings.MODID, "entity/aschParticle"));
    }
}
