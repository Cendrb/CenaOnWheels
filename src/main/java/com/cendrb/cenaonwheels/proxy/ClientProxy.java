package com.cendrb.cenaonwheels.proxy;

import com.cendrb.cenaonwheels.RefStrings;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(RefStrings.MODID + ":" + id, "inventory"));
    }
}
