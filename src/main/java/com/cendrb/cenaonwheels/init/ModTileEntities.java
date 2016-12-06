package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.RefStrings;
import com.cendrb.cenaonwheels.tileentity.TileEntityCowKlidGenerator;
import com.cendrb.cenaonwheels.tileentity.TileEntityKlidInfusionPlate;
import com.cendrb.cenaonwheels.tileentity.TileEntityMultiblockPart;
import com.cendrb.cenaonwheels.tileentity.TileEntityKlidStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ModTileEntities {

    public static void init()
    {
        register(TileEntityKlidStorage.class, "klidStorage");
        register(TileEntityMultiblockPart.class, "multiblockPart");
        register(TileEntityCowKlidGenerator.class, "cowKlidGenerator");
        register(TileEntityKlidInfusionPlate.class, "klidInfusionPlate");
    }

    private static void register(Class<? extends TileEntity> clazz, String name)
    {
        GameRegistry.registerTileEntity(clazz, RefStrings.MODID + ":" + name);
    }
}
