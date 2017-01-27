package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.Core;
import com.cendrb.cenaonwheels.entity.EntityAkbarCow;
import com.cendrb.cenaonwheels.entity.EntityKlidBurst;
import com.cendrb.cenaonwheels.entity.EntityMegaAkbarCow;
import net.minecraft.entity.passive.EntityCow;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by cendr_000 on 16.10.2016.
 */
public class ModEntities {
    public static void init()
    {
        EntityRegistry.registerModEntity(EntityKlidBurst.class, "KlidBurst", 0, Core.instance, 64, 3, true);
        EntityRegistry.registerModEntity(EntityAkbarCow.class, "AkbarCow", 1, Core.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntityMegaAkbarCow.class, "MegaAkbarCow", 2, Core.instance, 64, 1, true);
    }
}
