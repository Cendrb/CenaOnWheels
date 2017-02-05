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
    public static ItemBase aschGem;
    public static ItemBase timelessGunpowder;
    public static ItemWrench wrench;
    public static ItemBase klidDetector;

    public static void init() {
        tempusShard = register(new ItemBase("tempusShard"));
        klidProjector = register(new ItemBase("klidProjector"));
        klidManipulationCircuit = register(new ItemBase("klidManipulationCircuit"));
        klidMetal = register(new ItemBase("klidMetal"));
        klidGem = register(new ItemBase("klidGem"));
        aschGem = register(new ItemBase("aschGem"));
        timelessGunpowder = register(new ItemBase("timelessGunpowder"));
        wrench = register(new ItemWrench());
        klidDetector = register(new ItemBase("klidDetector"));
    }

    private static <T extends ItemBase> T register(T item) {
        GameRegistry.register(item);
        item.registerItemModel();
        return item;
    }
}
