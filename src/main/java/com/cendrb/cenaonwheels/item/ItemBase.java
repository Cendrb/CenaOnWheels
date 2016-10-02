package com.cendrb.cenaonwheels.item;

import com.cendrb.cenaonwheels.Core;
import com.cendrb.cenaonwheels.ItemModelProvider;
import com.cendrb.cenaonwheels.block.BlockBase;
import com.cendrb.cenaonwheels.init.ModCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ItemBase extends Item implements ItemModelProvider {

    private String pureName;

    public ItemBase(String pureName)
    {
        this.pureName = pureName;
        setUnlocalizedName(pureName);
        setRegistryName(pureName);
        setCreativeTab(ModCreativeTabs.cowCreativeTab);
    }

    @Override
    public void registerItemModel() {
        Core.proxy.registerItemRenderer(this, 0, pureName);
    }

    public String getPureName() {
        return pureName;
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
