package com.cendrb.cenaonwheels.proxy;

import net.minecraft.item.Item;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ServerProxy extends CommonProxy {
    @Override
    public void registerParticleTextureSticher() {
        // no rendering on server side
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        // no rendering on server side
    }

    @Override
    public void registerEntityRenderers() {
        // no rendering on server side
    }

    @Override
    public void registerTileEntitySpecialRenderers() {
        // no rendering on server side
    }
}
