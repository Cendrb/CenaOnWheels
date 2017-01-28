package com.cendrb.cenaonwheels.block;

import com.cendrb.cenaonwheels.Core;
import com.cendrb.cenaonwheels.ItemBlockModelProvider;
import com.cendrb.cenaonwheels.ItemModelProvider;
import com.cendrb.cenaonwheels.init.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class BlockBase extends Block implements ItemBlockModelProvider {

    private String pureName;

    public BlockBase(Material materialIn, String pureName) {
        super(materialIn);
        this.pureName = pureName;
        setUnlocalizedName(pureName);
        setRegistryName(pureName);
        setCreativeTab(ModCreativeTabs.cowCreativeTab);
        setHardness(2.0F);
        setHarvestLevel("pickaxe", 0);
    }

    public String getPureName() {
        return pureName;
    }

    @Override
    public void registerItemModel(Item item) {
        Core.proxy.registerItemRenderer(item, 0, pureName);
    }

    @Override
    public BlockBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
