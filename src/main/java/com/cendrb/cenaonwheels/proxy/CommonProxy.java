package com.cendrb.cenaonwheels.proxy;

import net.minecraft.item.Item;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public abstract class CommonProxy {
    public abstract void registerItemRenderer(Item item, int meta, String id);
    public abstract void registerEntityRederers();
}
