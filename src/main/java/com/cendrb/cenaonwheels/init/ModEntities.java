package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.Core;
import com.cendrb.cenaonwheels.entity.EntityKlidBurst;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by cendr_000 on 16.10.2016.
 */
public class ModEntities {
    public static void init()
    {
        EntityRegistry.registerModEntity(EntityKlidBurst.class, "KlidBurst", 0, Core.instance, 64, 3, true, 69, 420);
    }
}
