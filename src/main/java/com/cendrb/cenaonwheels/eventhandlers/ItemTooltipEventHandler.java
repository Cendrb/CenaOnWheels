package com.cendrb.cenaonwheels.eventhandlers;

import com.cendrb.cenaonwheels.KlidStorageBlockEnergyValues;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by cendr on 28/01/2017.
 */
public class ItemTooltipEventHandler {
    @SubscribeEvent
    public void onItemTooltipEvent(ItemTooltipEvent tooltipEvent) {
        Block hoveredBlock = Block.getBlockFromItem(tooltipEvent.getItemStack().getItem());
        if(hoveredBlock != null && KlidStorageBlockEnergyValues.getEnergyValue(hoveredBlock) != null)
        {
            tooltipEvent.getToolTip().add("Core klid value: " + KlidStorageBlockEnergyValues.getEnergyValue(hoveredBlock));
        }
    }
}
