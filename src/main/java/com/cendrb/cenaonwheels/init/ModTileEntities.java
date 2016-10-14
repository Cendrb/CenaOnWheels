package com.cendrb.cenaonwheels.init;

import com.cendrb.cenaonwheels.RefStrings;
import com.cendrb.cenaonwheels.tileentity.TileEntityMultiblockPart;
import com.cendrb.cenaonwheels.tileentity.TileEntityVisStorage;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by cendr_000 on 02.10.2016.
 */
public class ModTileEntities {

    public static void init()
    {
        GameRegistry.registerTileEntity(TileEntityVisStorage.class, RefStrings.MODID + ":" + "visStorage");
        GameRegistry.registerTileEntity(TileEntityMultiblockPart.class, RefStrings.MODID + ":" + "multiblockPart");
    }
}
