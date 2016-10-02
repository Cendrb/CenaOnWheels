package com.cendrb.cenaonwheels.creativetabs;

import com.cendrb.cenaonwheels.RefStrings;
import com.cendrb.cenaonwheels.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class COWCreativeTab extends CreativeTabs {
    public COWCreativeTab() {
        super(RefStrings.NAME);
    }

    @Override
    public Item getTabIconItem() {
        return ModItems.tempusShard;
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
