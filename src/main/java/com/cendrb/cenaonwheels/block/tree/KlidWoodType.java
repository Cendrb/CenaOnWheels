package com.cendrb.cenaonwheels.block.tree;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

/**
 * Created by cendr on 25/01/2017.
 */
public enum KlidWoodType implements IStringSerializable {
    ASCH(0, "asch", MapColor.RED);

    private static final KlidWoodType[] META_LOOKUP = new KlidWoodType[values().length];
    static {
        for (KlidWoodType woodType : values()) {
            META_LOOKUP[woodType.getMetadata()] = woodType;
        }
    }

    private final int meta;
    private final String name;
    private final MapColor mapColor;

    private KlidWoodType(int metaIn, String nameIn, MapColor mapColorIn) {
        this.meta = metaIn;
        this.name = nameIn;
        this.mapColor = mapColorIn;
    }

    public int getMetadata() {
        return this.meta;
    }

    public MapColor getMapColor() {
        return this.mapColor;
    }

    public String toString() {
        return this.name;
    }

    public static KlidWoodType getFromMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    public String getName() {
        return this.name;
    }

    public String getCapitalizedName()
    {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
