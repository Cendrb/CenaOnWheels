package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.creativetabs.COWCreativeTab;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ModCreativeTabs {
    public static COWCreativeTab cowCreativeTab;

    public static void init()
    {
        cowCreativeTab = new COWCreativeTab();
    }
}
