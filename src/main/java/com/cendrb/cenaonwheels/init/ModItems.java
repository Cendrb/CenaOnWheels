package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.item.ItemBase;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ModItems {

    public static ItemBase tempusShard;

    public static void init() {
        tempusShard = register(new ItemBase("tempusShard"));
    }

    private static <T extends ItemBase> T register(T item) {
        GameRegistry.register(item);
        item.registerItemModel();
        return item;
    }
}
