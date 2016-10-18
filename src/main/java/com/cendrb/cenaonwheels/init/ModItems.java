package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.item.ItemBase;
import com.cendrb.cenaonwheels.item.ItemWrench;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ModItems {

    public static ItemBase tempusShard;
    public static ItemBase klidProjector;
    public static ItemBase klidManipulationCircuit;
    public static ItemBase klidMetal;
    public static ItemBase klidGem;
    public static ItemWrench wrench;

    public static void init() {
        tempusShard = register(new ItemBase("tempusShard"));
        klidProjector = register(new ItemBase("klidProjector"));
        klidManipulationCircuit = register(new ItemBase("klidManipulationCircuit"));
        klidMetal = register(new ItemBase("klidMetal"));
        klidGem = register(new ItemBase("klidGem"));
        wrench = register(new ItemWrench());
    }

    private static <T extends ItemBase> T register(T item) {
        GameRegistry.register(item);
        item.registerItemModel();
        return item;
    }
}
