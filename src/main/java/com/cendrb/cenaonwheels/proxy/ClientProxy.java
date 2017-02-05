package com.cendrb.cenaonwheels.proxy;

import com.cendrb.cenaonwheels.RefStrings;
import com.cendrb.cenaonwheels.entity.EntityKlidBurst;
import com.cendrb.cenaonwheels.entity.render.KlidBurstRenderFactory;
import com.cendrb.cenaonwheels.eventhandlers.OverlayHandlerOverlay;
import com.cendrb.cenaonwheels.eventhandlers.ParticleTextureSticherHandler;
import com.cendrb.cenaonwheels.tileentity.TileEntityKlidInfusionPlate;
import com.cendrb.cenaonwheels.tileentity.render.TileEntityRendererKlidInfusionPlate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void registerParticleTextureSticher() {
        MinecraftForge.EVENT_BUS.register(new ParticleTextureSticherHandler());
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(RefStrings.MODID + ":" + id, "inventory"));
    }

    @Override
    public void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityKlidBurst.class, new KlidBurstRenderFactory());
    }

    @Override
    public void registerTileEntitySpecialRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKlidInfusionPlate.class, new TileEntityRendererKlidInfusionPlate());
    }

    @Override
    public void registerHUDRenderer() {
        MinecraftForge.EVENT_BUS.register(new OverlayHandlerOverlay(Minecraft.getMinecraft()));
    }
}
